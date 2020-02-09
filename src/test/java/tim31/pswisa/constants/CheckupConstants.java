package tim31.pswisa.constants;

import java.time.LocalDate;

public class CheckupConstants {


	public static final Long ID_1 = 1L;
	public static final Long ID_2 = 2L;
	public static final Long ID_3 = 3L;

	public static final double DISCOUNT_1 = 20;
	public static final double DISCOUNT_2 = 0;
	public static final double DISCOUNT_3 = 0;
	
	public static final boolean SCHEDULED_1 = false;
	public static final boolean SCHEDULED_2 = false;
	public static final boolean SCHEDULED_3 = false;
	
	public static final LocalDate LOCAL_DATE_1 = LocalDate.parse("2020-02-23");
	public static final LocalDate LOCAL_DATE_2 = LocalDate.parse("2020-01-23");
	public static final LocalDate LOCAL_DATE_3 = LocalDate.parse("2020-01-12");
	
	
	public static final String TIME_1 = "1";
	public static final String TIME_2 = "2";
	public static final String TIME_3 = "3";
	
	public static final String TIP_1 = "KARDIOLOSKI";
	public static final String TIP_2 = "KARDIOLOSKI";
	public static final String TIP_3 = "DERMATOLOSKI";
	
	public static final boolean FREE_1 = false;
	public static final boolean FREE_2 = false;
	public static final boolean FREE_3 = true;
	
	public static final boolean FINISHED_1 = false;
	public static final boolean FINISHED_2 = false;
	public static final boolean FINISHED_3 = false;
	
	public static final int DURATION_1 = 1;
	
	public static final double PRICE_1 = 100;
	public static final double PRICE_2 = 100;
	public static final double PRICE_3 = 50;
	
	public static final int REZ_1 = 3;
	
	public static final String TIP_PREGLEDA_1 = "KARDIOLOSKI";
	public static final String TIP_PREGLEDA_2 = "DERMATOLOSKI";

	public static final Long CHECKUP_ID = 1L;
	public static final Long CHECKUP_ID2 = 2L;
	public static final Long CHECKUP_ID_FALSE = 1000L;
	public static final String CHECKUP_CHTYPE = "KARDIOLOSKI";
	public static final boolean CHECKUP_SCHEDULED = false;
	public static final LocalDate CHECKUP_DATE = LocalDate.parse("2020-02-02");
	public static final String CHECKUP_TIME = "13";
	public static final String CHECKUP_TIME_FALSE = "15";
	public static final Integer CHECKUP_PRICE = 100;
	public static final String CHECKUP_CLINIC_NAME = "Naziv klinike";
	public static final String CHECKUP_ROOM_NAME = "Naziv sobe";
	public static final String CHECKUP_CLINIC_ADDRESS = "Adresa klinike";
	public static final double CHECKUP_DISCOUNT = 10;
	public static final int CHECKUP_DURATION = 1;
	public static final boolean CHECKUP_FINISHED = false;
	public static final int CHECKUP_COUNT =  1; // number of checkups in one room in given date

	// for checkup request
	public static final LocalDate CHECKUP_DATER = LocalDate.parse("2020-02-10");
	
}

