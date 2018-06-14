package com.fw.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.fw.beans.AddressBean;
import com.fw.beans.FromToAddressComponent;
import com.fw.enums.AddressComponentTypes;
import com.fw.services.impl.LoadRequestServiceImpl;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class LocationUtils {

	public static int count = 0;
	public final static Logger logger = Logger.getLogger(LocationUtils.class);

	public static JSONArray getCityNames(AddressBean addressBean) {

		FromToAddressComponent[] fromAddressComponent = addressBean.getFrom().getLocationJSON().getAddress_components();
		FromToAddressComponent[] toAddressComponent = addressBean.getTo().getLocationJSON().getAddress_components();

		String sourceCity = LoadRequestUtils.getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.administrative_area_level_2.toDbString());
		String sourceState = LoadRequestUtils.getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.administrative_area_level_1.toDbString());
		String sourceCountry = LoadRequestUtils.getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.country.toDbString());
		
		String destinationCity = LoadRequestUtils.getAddressComponentByType(toAddressComponent,
				AddressComponentTypes.administrative_area_level_2.toDbString());
		String destinationState = LoadRequestUtils.getAddressComponentByType(toAddressComponent,
				AddressComponentTypes.administrative_area_level_1.toDbString());
		String destinationCountry = LoadRequestUtils.getAddressComponentByType(toAddressComponent,
				AddressComponentTypes.country.toDbString());
		
		String sourceString = sourceCity + "," + sourceState + "," + sourceCountry;
		String destinationString = destinationCity + "," + destinationState + "," + destinationCountry;

		JSONArray jsonArray = getCityNames(sourceString, destinationString);
		return jsonArray;
	}

	private static JSONArray getCityNames(String sourceString, String destinationString) {
		logger.info(">>>>>>>>> sourceString" + sourceString);
		logger.info(">>>>>>>>> destinationString" + destinationString);
		Long startTime = System.currentTimeMillis();
		JSONArray returnArray = new JSONArray();
		sourceString = sourceString.replaceAll(",", "+").replaceAll(" ", "%20");
		destinationString = destinationString.replaceAll(",", "+").replaceAll(" ", "%20");
		String json = NetworkUtils.getJSONDataFromUrl(
				"https://maps.googleapis.com/maps/api/directions/json?origin=" + sourceString + "&destination="
						+ destinationString + "&alternatives=true&key=AIzaSyCJtDvf2IFk6XBmFm5FwywENrvVJ71_1ds");
		Map<String, Integer> outerCityMap = new LinkedHashMap<>();
		JSONObject jsonObj;
		try {
			ArrayList<ArrayList<String>> mainList = new ArrayList<>();
			jsonObj = new JSONObject(json);
			JSONArray jsonRoutesArray = jsonObj.getJSONArray("routes");
			logger.info("Number Of Routes : " + jsonRoutesArray.length());
			for (int outer = 0; outer < jsonRoutesArray.length(); outer++) {
				Map<String, Integer> innerCityMap = new LinkedHashMap<>();

				JSONObject routesObject = jsonRoutesArray.getJSONObject(outer);
				JSONArray jsonLegsArray = routesObject.getJSONArray("legs");
				JSONObject legsObject = jsonLegsArray.getJSONObject(0);
				JSONArray stepsArray = legsObject.getJSONArray("steps");
				String lastCity = "";
				String currentCity = "";
				Integer distance = 0;
				for (int i = 0; i < stepsArray.length(); i++) {
					JSONObject stepObj = stepsArray.getJSONObject(i);
					JSONObject startLocObj = stepObj.getJSONObject("start_location");
					JSONObject endLocObj = stepObj.getJSONObject("end_location");
					ArrayList<String> latLongList = getLatLongFromJSONObject(startLocObj);
					distance = distance + (Integer) stepObj.getJSONObject("distance").get("value");
					logger.info("distance=" + distance);
					String lat = latLongList.get(0);
					String lon = latLongList.get(1);
					ArrayList<String> currentCityArray = testFunction2(lat, lon);
					if (currentCityArray.size() > 1) {
						for (String loopCity : currentCityArray)
							innerCityMap.put(loopCity, distance);
					} else if (currentCityArray.size() == 1)
						innerCityMap.put(currentCityArray.get(0), distance);

				}
				outerCityMap.putAll(innerCityMap);
			}

			List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(
					outerCityMap.entrySet());
			Collections.sort(entries, new CustomizedHashMap());
			Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
			for (Map.Entry<String, Integer> entry : entries) {
				sortedMap.put(entry.getKey(), entry.getValue());
				System.out.print(sortedMap.put(entry.getKey(), entry.getValue()) + "  ");
			}

			logger.info(
					"-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
			Map.Entry<String, Integer> entry = sortedMap.entrySet().iterator().next();
			Integer offset = entry.getValue();
			for (Entry<String, Integer> currentEntry : sortedMap.entrySet()) {

				currentEntry.setValue((currentEntry.getValue() - offset));
			}
			for (Entry<String, Integer> currentEntry : sortedMap.entrySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", currentEntry.getKey());
				jsonObject.put("distance", currentEntry.getValue());
				returnArray.put(jsonObject);
			}
			logger.info("JSON OBJ--->" + returnArray.toString());
			logger.info("RESULT (UNIQUE & ORDERED):-" + sortedMap);
			Long endTime = System.currentTimeMillis();
			logger.info("TOTAL TIME (in seconds):  " + ((endTime - startTime) / 1000.000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnArray;
	}

	private static ArrayList<String> getLatLongFromJSONObject(JSONObject obj) throws Exception {
		ArrayList<String> latLongList = new ArrayList<String>();
		String lat = obj.getString("lat");
		String lng = obj.getString("lng");
		latLongList.add(lat);
		latLongList.add(lng);
		return latLongList;
	}

	private static ArrayList<String> testFunction2(String lat, String lng) throws Exception {
		ArrayList<String> cityArray = new ArrayList<>();
		Double latD = Double.parseDouble(lat);
		Double lngD = Double.parseDouble(lng);
		count++;
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDWb6GWLCfyqet3grhVyPRY555uQEY9VDk");
		GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(new LatLng(latD, lngD)).language("en")
				.resultType(AddressType.COUNTRY, AddressType.LOCALITY).await();

		for (GeocodingResult r : results) {
			for (AddressComponent ac : r.addressComponents) {
				for (AddressComponentType acType : ac.types) {

					if (acType == AddressComponentType.LOCALITY) {
						logger.info("CITY=" + ac.longName);
						cityArray.add(ac.longName);
					} else if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2) {
						// logger.info("STATE=" + ac.longName);

					} else if (acType == AddressComponentType.POSTAL_CODE) {
					}
				}

			}
		}
		return cityArray;
	}

}
