package com.fw.beans;

/**
 * This bean is a container to pass some configurations to client as per
 * requirements.
 * 
 * @author Vikas Sonwal
 */
public class ClientConfigBean {

    private MaterialTypes materialTypes = new MaterialTypes();
    private ContactType contactType = new ContactType();
    private LengthUnits lengthUnits = new LengthUnits();
    private UserRoles userRoles = new UserRoles();
    private WeightUnits weightUnits = new WeightUnits();
    private LoadRequestStatus loadRequestStatus = new LoadRequestStatus();
    private BidStatus bidStatus = new BidStatus();
    private Facts facts = new Facts();
    private ContactStatus contactStatus = new ContactStatus();
    private TruckType truckTypes = new TruckType();
    private CallStatus callStatus = new CallStatus();
    private RatingType ratingType = new RatingType();

    public MaterialTypes getMaterialTypes() {
		return materialTypes;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public LengthUnits getLengthUnits() {
		return lengthUnits;
	}

	public UserRoles getUserRoles() {
		return userRoles;
	}

	public WeightUnits getWeightUnits() {
		return weightUnits;
	}

	public LoadRequestStatus getLoadRequestStatus() {
		return loadRequestStatus;
	}

	public BidStatus getBidStatus() {
		return bidStatus;
	}

	public Facts getFacts() {
		return facts;
	}

	public ContactStatus getContactStatus() {
		return contactStatus;
	}

	public TruckType getTruckTypes() {
		return truckTypes;
	}

	public CallStatus getCallStatus() {
		return callStatus;
	}

	public RatingType getRatingType() {
		return ratingType;
	}

	class MaterialTypes {
		private final String solid = com.fw.enums.MaterialTypes.SOLID.toDbString();
		private final String liquid = com.fw.enums.MaterialTypes.LIQUID.toDbString();
		private final String fragile = com.fw.enums.MaterialTypes.FRAGILE.toDbString();
	    private final String biomaterials = com.fw.enums.MaterialTypes.BIOMATERIALS.toDbString();
	    private final String ceramics = com.fw.enums.MaterialTypes.CERAMICS.toDbString();
	    private final String composites = com.fw.enums.MaterialTypes.COMPOSITES.toDbString();
	    private final String concrete = com.fw.enums.MaterialTypes.CONCRETE.toDbString();
	    private final String electronic = com.fw.enums.MaterialTypes.ELECTRONIC.toDbString();
	    private final String optical = com.fw.enums.MaterialTypes.OPTICAL.toDbString();
	    private final String glass = com.fw.enums.MaterialTypes.GLASS.toDbString();
	    private final String metals = com.fw.enums.MaterialTypes.METALS.toDbString();
	    private final String meta_materials = com.fw.enums.MaterialTypes.META_MATERIALS.toDbString();
	    private final String nano_materials = com.fw.enums.MaterialTypes.NANO_MATERIALS.toDbString();
	    private final String polymers = com.fw.enums.MaterialTypes.POLYMERS.toDbString();
	    private final String plastics = com.fw.enums.MaterialTypes.PLASTICS.toDbString();
	    private final String semiconductors = com.fw.enums.MaterialTypes.SEMICONDUCTORS.toDbString();
	    private final String wood = com.fw.enums.MaterialTypes.WOOD.toDbString();
	    private final String textiles = com.fw.enums.MaterialTypes.TEXTILES.toDbString();
	    private final String adhesive = com.fw.enums.MaterialTypes.ADHESIVE.toDbString();
	    private final String documents = com.fw.enums.MaterialTypes.DOCUMENTS.toDbString();
	    private final String chemicals = com.fw.enums.MaterialTypes.CHEMICALS.toDbString();
	    private final String radioactive = com.fw.enums.MaterialTypes.RADIOACTIVE.toDbString();
	    private final String other = com.fw.enums.MaterialTypes.OTHER.toDbString();
		
		public String getSolid() {
			return solid;
		}
		public String getLiquid() {
			return liquid;
		}
		public String getFragile() {
			return fragile;
		}
		public String getBiomaterials() {
			return biomaterials;
		}
		public String getCeramics() {
			return ceramics;
		}
		public String getComposites() {
			return composites;
		}
		public String getConcrete() {
			return concrete;
		}
		public String getElectronic() {
			return electronic;
		}
		public String getOptical() {
			return optical;
		}
		public String getGlass() {
			return glass;
		}
		public String getMetals() {
			return metals;
		}
		public String getMeta_materials() {
			return meta_materials;
		}
		public String getNano_materials() {
			return nano_materials;
		}
		public String getPolymers() {
			return polymers;
		}
		public String getPlastics() {
			return plastics;
		}
		public String getSemiconductors() {
			return semiconductors;
		}
		public String getWood() {
			return wood;
		}
		public String getTextiles() {
			return textiles;
		}
		public String getAdhesive() {
			return adhesive;
		}
		public String getDocuments() {
			return documents;
		}
		public String getChemicals() {
			return chemicals;
		}
		public String getRadioactive() {
			return radioactive;
		}
		public String getOther() {
			return other;
		}
	}
	
	class ContactType {
		private final String email = com.fw.enums.ContactType.EMAIL.toDbString();
		private final String sms = com.fw.enums.ContactType.SMS.toDbString();
		private final String call = com.fw.enums.ContactType.CALL.toDbString();
		private final String whatsapp = com.fw.enums.ContactType.WHATSAPP.toDbString();
		
		public String getEmail() {
			return email;
		}
		public String getSms() {
			return sms;
		}
		public String getCall() {
			return call;
		}
		public String getWhatsapp() {
			return whatsapp;
		}
	}
	
	class LengthUnits {
		private final String feet = com.fw.enums.LengthUnits.FEET.toDbString();
		private final String meter = com.fw.enums.LengthUnits.METER.toDbString();
		private final String centimeter = com.fw.enums.LengthUnits.CENTIMETER.toDbString();
		
		public String getFeet() {
			return feet;
		}
		public String getMeter() {
			return meter;
		}
		public String getCentimeter() {
			return centimeter;
		}
	}
	
	class UserRoles {
		private final String csr = com.fw.enums.UserRoles.CSR.toDbString();
		private final String load_provider = com.fw.enums.UserRoles.LOAD_PROVIDER.toDbString();
		private final String capacity_provider = com.fw.enums.UserRoles.CAPACITY_PROVIDER.toDbString();
		private final String both = com.fw.enums.UserRoles.BOTH.toDbString();
		private final String super_admin = com.fw.enums.UserRoles.SUPER_ADMIN.toDbString();
		
		public String getCsr() {
			return csr;
		}
		public String getLoad_provider() {
			return load_provider;
		}
		public String getCapacity_provider() {
			return capacity_provider;
		}
		public String getBoth() {
			return both;
		}
		public String getSuper_admin() {
			return super_admin;
		}
	}
	
	class WeightUnits {
		private final String kilogram = com.fw.enums.WeightUnits.KILOGRAM.toDbString();
		private final String ton = com.fw.enums.WeightUnits.TON.toDbString();
		private final String kilolitre = com.fw.enums.WeightUnits.KILOLITRE.toDbString();
		
		public String getKilogram() {
			return kilogram;
		}
		public String getTon() {
			return ton;
		}
		public String getKilolitre() {
			return kilolitre;
		}
	}
	
	class LoadRequestStatus {
		private final String delivered = com.fw.enums.LoadRequestStatus.DELIVERED.getValue();
		private final String cancelled = com.fw.enums.LoadRequestStatus.CANCELLED.getValue();
		private final String request_on_hold = com.fw.enums.LoadRequestStatus.REQUEST_ON_HOLD.getValue();
		private final String bidding_in_progress = com.fw.enums.LoadRequestStatus.BIDDING_IN_PROGRESS.getValue();
		private final String booked = com.fw.enums.LoadRequestStatus.BOOKED.getValue();
		private final String requested = com.fw.enums.LoadRequestStatus.REQUESTED.getValue();
		private final String awarded = com.fw.enums.LoadRequestStatus.AWARDED.getValue();
		private final String expired = com.fw.enums.LoadRequestStatus.EXPIRED.getValue();
		private final String blocked_request = com.fw.enums.LoadRequestStatus.BLOCKED_REQUEST.getValue();
		private final String resume = com.fw.enums.LoadRequestStatus.RESUME.getValue();
		
		
		public String getDelivered() {
			return delivered;
		}
		public String getCancelled() {
			return cancelled;
		}
		public String getBidding_in_progress() {
			return bidding_in_progress;
		}
		public String getBooked() {
			return booked;
		}
		public String getRequested() {
			return requested;
		}
		public String getAwarded() {
			return awarded;
		}
		public String getExpired() {
			return expired;
		}
		public String getRequest_on_hold() {
			return request_on_hold;
		}
		public String getBlocked_request() {
			return blocked_request;
		}
		public String getResume() {
			return resume;
		}
		
		
	}
	
	class BidStatus {
		
		private final String active = com.fw.enums.BidStatus.ACTIVE.toDbString();
		private final String load_request_cancelled = com.fw.enums.BidStatus.LOAD_REQUEST_CANCELLED.toDbString();
		private final String not_awarded = com.fw.enums.BidStatus.NOT_AWARDED.toDbString();
		private final String awarded = com.fw.enums.BidStatus.AWARDED.toDbString();
		
		public String getActive() {
			return active;
		}
		public String getLoad_request_cancelled() {
			return load_request_cancelled;
		}
		public String getNot_awarded() {
			return not_awarded;
		}
		public String getAwarded() {
			return awarded;
		}
	}
	
	class Facts {
		private final String phone_no = com.fw.enums.Facts.PHONE_NO.toDbString();
		private final String email_id = com.fw.enums.Facts.EMAIL_ID.toDbString();
		private final String first_name = com.fw.enums.Facts.FIRST_NAME.toDbString();
		private final String middle_name = com.fw.enums.Facts.MIDDLE_NAME.toDbString();
		private final String last_name = com.fw.enums.Facts.LAST_NAME.toDbString();
		private final String company_name = com.fw.enums.Facts.COMPANY_NAME.toDbString();
		private final String no_of_trucks = com.fw.enums.Facts.NO_OF_TRUCKS.toDbString();
		
		public String getPhone_no() {
			return phone_no;
		}
		public String getEmail_id() {
			return email_id;
		}
		public String getFirst_name() {
			return first_name;
		}
		public String getMiddle_name() {
			return middle_name;
		}
		public String getLast_name() {
			return last_name;
		}
		public String getCompany_name() {
			return company_name;
		}
		public String getNo_of_trucks() {
			return no_of_trucks;
		}
	}
	
	class ContactStatus {
		private final String pending = com.fw.enums.ContactStatus.PENDING.toDbString();
		private final String sms_canceled = com.fw.enums.ContactStatus.SMS_CANCELED.toDbString(); 
		private final String sms_success = com.fw.enums.ContactStatus.SMS_SUCCESS.toDbString(); 
		private final String sms_failure = com.fw.enums.ContactStatus.SMS_FAILURE.toDbString(); 
		private final String call_success = com.fw.enums.ContactStatus.CALL_SUCCESS.toDbString(); 
		private final String call_failure = com.fw.enums.ContactStatus.CALL_FAILURE.toDbString(); 
		private final String missed_call = com.fw.enums.ContactStatus.MISSED_CALL.toDbString(); 
		private final String interested = com.fw.enums.ContactStatus.INTERESTED.toDbString(); 
		private final String not_interested = com.fw.enums.ContactStatus.NOT_INTERESTED.toDbString(); 
		private final String no_call = com.fw.enums.ContactStatus.NO_CALL.toDbString(); 
		private final String hang_up = com.fw.enums.ContactStatus.HANG_UP.toDbString(); 
		private final String email_sent = com.fw.enums.ContactStatus.EMAIL_SENT.toDbString(); 
		private final String email_failure = com.fw.enums.ContactStatus.EMAIL_FAILURE.toDbString(); 
		private final String got_email_replied = com.fw.enums.ContactStatus.GOT_EMAIL_REPLIED.toDbString(); 
		private final String sms_status_awaited = com.fw.enums.ContactStatus.SMS_STATUS_AWAITED.toDbString();
		
		public String getPending() {
			return pending;
		}
		public String getSms_canceled() {
			return sms_canceled;
		}
		public String getSms_success() {
			return sms_success;
		}
		public String getSms_failure() {
			return sms_failure;
		}
		public String getCall_success() {
			return call_success;
		}
		public String getCall_failure() {
			return call_failure;
		}
		public String getMissed_call() {
			return missed_call;
		}
		public String getInterested() {
			return interested;
		}
		public String getNot_interested() {
			return not_interested;
		}
		public String getNo_call() {
			return no_call;
		}
		public String getHang_up() {
			return hang_up;
		}
		public String getEmail_sent() {
			return email_sent;
		}
		public String getEmail_failure() {
			return email_failure;
		}
		public String getGot_email_replied() {
			return got_email_replied;
		}
		public String getSms_status_awaited() {
			return sms_status_awaited;
		}
	}
	
	class TruckType {
		private final String small_mini_truck = com.fw.enums.TruckType.SMALL_MINI_TRUCK.toDbString();
		private final String light_minivan = com.fw.enums.TruckType.LIGHT_MINIVAN.toDbString();
		private final String light_sport_utility_vehicle = com.fw.enums.TruckType.LIGHT_SPORT_UTILITY_VEHICLE.toDbString();
		private final String light_canopy_express = com.fw.enums.TruckType.LIGHT_CANOPY_EXPRESS.toDbString();
		private final String light_pickup_truck = com.fw.enums.TruckType.LIGHT_PICKUP_TRUCK.toDbString();
		private final String light_panel_truck = com.fw.enums.TruckType.LIGHT_PANEL_TRUCK.toDbString();
		private final String light_cab_forward = com.fw.enums.TruckType.LIGHT_CAB_FORWARD.toDbString();
		private final String light_tow_truck = com.fw.enums.TruckType.LIGHT_TOW_TRUCK.toDbString();
		private final String light_panel_van = com.fw.enums.TruckType.LIGHT_PANEL_VAN.toDbString();
		private final String light_sedan_delivery = com.fw.enums.TruckType.LIGHT_SEDAN_DELIVERY.toDbString();
		private final String medium_box_truck = com.fw.enums.TruckType.MEDIUM_BOX_TRUCK.toDbString();
		private final String medium_van = com.fw.enums.TruckType.MEDIUM_VAN.toDbString();
		private final String medium_cutaway_van_chasis = com.fw.enums.TruckType.MEDIUM_CUTAWAY_VAN_CHASIS.toDbString();
		private final String medium_duty_truck = com.fw.enums.TruckType.MEDIUM_DUTY_TRUCK.toDbString();
		private final String medium_standard_truck = com.fw.enums.TruckType.MEDIUM_STANDARD_TRUCK.toDbString();
		private final String medium_platform_truck = com.fw.enums.TruckType.MEDIUM_PLATFORM_TRUCK.toDbString();
		private final String medium_flatbed_truck = com.fw.enums.TruckType.MEDIUM_FLATBED_TRUCK.toDbString();
		private final String medium_fire_truck = com.fw.enums.TruckType.MEDIUM_FIRE_TRUCK.toDbString();
		private final String medium_motorhome = com.fw.enums.TruckType.MEDIUM_MOTORHOME.toDbString();
		private final String heavy_concrete_transport_truck = com.fw.enums.TruckType.HEAVY_CONCRETE_TRANSPORT_TRUCK.toDbString();
		private final String heavy_mobile_crane = com.fw.enums.TruckType.HEAVY_MOBILE_CRANE.toDbString();
		private final String heavy_dump_truck = com.fw.enums.TruckType.HEAVY_DUMP_TRUCK.toDbString();
		private final String heavy_garbage_truck = com.fw.enums.TruckType.HEAVY_GARBAGE_TRUCK.toDbString();
		private final String heavy_log_carrier = com.fw.enums.TruckType.HEAVY_LOG_CARRIER.toDbString();
		private final String heavy_refrigerator_truck = com.fw.enums.TruckType.HEAVY_REFRIGERATOR_TRUCK.toDbString();
		private final String heavy_tractor_unit = com.fw.enums.TruckType.HEAVY_TRACTOR_UNIT.toDbString();
		private final String heavy_tank_truck = com.fw.enums.TruckType.HEAVY_TANK_TRUCK.toDbString();
		private final String very_heavy_alma_transporter = com.fw.enums.TruckType.VERY_HEAVY_ALMA_TRANSPORTER.toDbString();
		private final String very_heavy_ballast_tractor = com.fw.enums.TruckType.VERY_HEAVY_BALLAST_TRACTOR.toDbString();
		private final String very_heavy_heavy_hauler = com.fw.enums.TruckType.VERY_HEAVY_HEAVY_HAULER.toDbString();
		private final String very_heavy_haul_truck = com.fw.enums.TruckType.VERY_HEAVY_HAUL_TRUCK.toDbString();
		
		public String getSmall_mini_truck() {
			return small_mini_truck;
		}
		public String getLight_minivan() {
			return light_minivan;
		}
		public String getLight_sport_utility_vehicle() {
			return light_sport_utility_vehicle;
		}
		public String getLight_canopy_express() {
			return light_canopy_express;
		}
		public String getLight_pickup_truck() {
			return light_pickup_truck;
		}
		public String getLight_panel_truck() {
			return light_panel_truck;
		}
		public String getLight_cab_forward() {
			return light_cab_forward;
		}
		public String getLight_tow_truck() {
			return light_tow_truck;
		}
		public String getLight_panel_van() {
			return light_panel_van;
		}
		public String getLight_sedan_delivery() {
			return light_sedan_delivery;
		}
		public String getMedium_box_truck() {
			return medium_box_truck;
		}
		public String getMedium_van() {
			return medium_van;
		}
		public String getMedium_cutaway_van_chasis() {
			return medium_cutaway_van_chasis;
		}
		public String getMedium_duty_truck() {
			return medium_duty_truck;
		}
		public String getMedium_standard_truck() {
			return medium_standard_truck;
		}
		public String getMedium_platform_truck() {
			return medium_platform_truck;
		}
		public String getMedium_flatbed_truck() {
			return medium_flatbed_truck;
		}
		public String getMedium_fire_truck() {
			return medium_fire_truck;
		}
		public String getMedium_motorhome() {
			return medium_motorhome;
		}
		public String getHeavy_concrete_transport_truck() {
			return heavy_concrete_transport_truck;
		}
		public String getHeavy_mobile_crane() {
			return heavy_mobile_crane;
		}
		public String getHeavy_dump_truck() {
			return heavy_dump_truck;
		}
		public String getHeavy_garbage_truck() {
			return heavy_garbage_truck;
		}
		public String getHeavy_log_carrier() {
			return heavy_log_carrier;
		}
		public String getHeavy_refrigerator_truck() {
			return heavy_refrigerator_truck;
		}
		public String getHeavy_tractor_unit() {
			return heavy_tractor_unit;
		}
		public String getHeavy_tank_truck() {
			return heavy_tank_truck;
		}
		public String getVery_heavy_alma_transporter() {
			return very_heavy_alma_transporter;
		}
		public String getVery_heavy_ballast_tractor() {
			return very_heavy_ballast_tractor;
		}
		public String getVery_heavy_heavy_hauler() {
			return very_heavy_heavy_hauler;
		}
		public String getVery_heavy_haul_truck() {
			return very_heavy_haul_truck;
		}
	}
	
	class CallStatus {
		
	    private final String call_success = com.fw.enums.CallStatus.CALL_SUCCESS.toDbString();
	    private final String call_failure = com.fw.enums.CallStatus.CALL_FAILURE.toDbString();
	    private final String missed_call = com.fw.enums.CallStatus.MISSED_CALL.toDbString();
	    private final String no_call = com.fw.enums.CallStatus.NO_CALL.toDbString();
	    private final String hang_up = com.fw.enums.CallStatus.HANG_UP.toDbString();
	    
		public String getCall_success() {
			return call_success;
		}
		public String getCall_failure() {
			return call_failure;
		}
		public String getMissed_call() {
			return missed_call;
		}
		public String getNo_call() {
			return no_call;
		}
		public String getHang_up() {
			return hang_up;
		}
	}
	
	class RatingType {
	    private final String not_rated = com.fw.enums.RatingType.NOT_RATED.toDbString();
	    private final String crap = com.fw.enums.RatingType.CRAP.toDbString();
	    private final String worst = com.fw.enums.RatingType.WORST.toDbString();
	    private final String bad = com.fw.enums.RatingType.BAD.toDbString();
	    private final String ok = com.fw.enums.RatingType.OK.toDbString();
	    private final String good = com.fw.enums.RatingType.GOOD.toDbString();
	    private final String better = com.fw.enums.RatingType.BETTER.toDbString();
	    private final String best = com.fw.enums.RatingType.BEST.toDbString();
	    private final String excellent = com.fw.enums.RatingType.EXCELLENT.toDbString();
	    
		public String getNot_rated() {
			return not_rated;
		}
		public String getCrap() {
			return crap;
		}
		public String getWorst() {
			return worst;
		}
		public String getBad() {
			return bad;
		}
		public String getOk() {
			return ok;
		}
		public String getGood() {
			return good;
		}
		public String getBetter() {
			return better;
		}
		public String getBest() {
			return best;
		}
		public String getExcellent() {
			return excellent;
		}
	    
	}
	
	class PaymentDirection{
		
		private final String credit = com.fw.enums.PaymentDirection.CREDIT.toDbString();
		private final String debit = com.fw.enums.PaymentDirection.DEBIT.toDbString();
		
		
		public String getCredit() {
			return credit;
		}
		public String getDebit() {
			return debit;
		}
	}
	class PaymentReceiveMethod{
		
		private final String direcDeposit = com.fw.enums.PaymentReceiveMethod.DIRECT_DEPOSIT.toDbString();
		private final String paymentGateway = com.fw.enums.PaymentReceiveMethod.PAYMENT_GATEWAY.toDbString();
		private final String upi = com.fw.enums.PaymentReceiveMethod.UPI.toDbString();
		
		public String getDirecDeposit() {
			return direcDeposit;
		}
		public String getPaymentGateway() {
			return paymentGateway;
		}
		public String getUpi() {
			return upi;
		}
		
		
	}
}