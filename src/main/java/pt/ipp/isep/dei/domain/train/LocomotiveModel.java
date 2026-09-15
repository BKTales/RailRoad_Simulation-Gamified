package pt.ipp.isep.dei.domain.train;

import java.io.Serializable;

/**
 * Enum representing various locomotive models.
 * Each locomotive has historical and technical data, such as:
 * - Year it became available
 * - Acquisition price
 * - Top speed
 * - Type of locomotive (Steam, Diesel, Electric)
 */
public enum LocomotiveModel implements Serializable {


    TREVITHICK_1(1800, 10000, 10,6000,7543, "TREVITHICK 1", LocomotiveType.STEAM_LOCOMOTIVE),
    STEPHENSON_ROCKET(1829, 16000, 15,6000,7992, "STEPHENSON ROCKET", LocomotiveType.STEAM_LOCOMOTIVE),
    JOHN_BULL(1831, 23000, 25,7000,9256, "JOHN BULL", LocomotiveType.STEAM_LOCOMOTIVE),
    DEWITT_CLINTON(1833,18000,20,5000,7187,"DEWITT CLINTON",LocomotiveType.STEAM_LOCOMOTIVE),
    PRUSSIAN(1837, 35000, 30,8000,10822, "PRUSSIAN", LocomotiveType.STEAM_LOCOMOTIVE),
    AMERICAN_C(1848,46000,42,5000,12606,"AMERICAN-C",LocomotiveType.STEAM_LOCOMOTIVE),
    IRON_DUKE(1855,78000,54,9000,30736,"IRON DUKE",LocomotiveType.STEAM_LOCOMOTIVE),
    A3(1865,67000,30,6000,0,"A3",LocomotiveType.STEAM_LOCOMOTIVE),
    EIGHT_WHEELER(1868,59000,48,5000,17808,"EIGHT WHEELER",LocomotiveType.STEAM_LOCOMOTIVE),
    VULCAN(1872, 32000, 30,4000,6393 ,"VULCAN", LocomotiveType.STEAM_LOCOMOTIVE),
    CONSOLIDATION(1877,51000,40,8000,19512,"CONSOLIDATION",LocomotiveType.STEAM_LOCOMOTIVE),
    TRUCK_SHAY(1882,43000,15,17000,18227,"TRUCK SHAY",LocomotiveType.STEAM_LOCOMOTIVE),
    MASTODON(1890,60000,45,13000,22395,"MASTODON",LocomotiveType.STEAM_LOCOMOTIVE),
    TEN_WHEELER(1892,66000,50,11000,21727,"TEN WHEELER",LocomotiveType.STEAM_LOCOMOTIVE),
    MOGUL(1895,83000,50,12000,25131,"MOGUL",LocomotiveType.STEAM_LOCOMOTIVE),
    BOBO(1895,85000,55,6000,33116,"BOBO",LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    ATLANTIC(1902, 93000, 80,18000,43556, "ATLANTIC", LocomotiveType.STEAM_LOCOMOTIVE),
    CAMELBACK(1905,75000,30,9000,15000,"CAMELBACK",LocomotiveType.STEAM_LOCOMOTIVE),
    PACIFIC(1908,119000,95,21000,62516,"PACIFIC",LocomotiveType.STEAM_LOCOMOTIVE),
    CLASS_G10(1910,98000,50,38000,50521,"CLASS G10",LocomotiveType.STEAM_LOCOMOTIVE),
    PRAIRIE(1912,85000,60,11000,34157,"PRAIRIE",LocomotiveType.STEAM_LOCOMOTIVE),
    D16SB(1914,65000,45,9000,21024,"D16SB",LocomotiveType.STEAM_LOCOMOTIVE),
    CLASS_13H(1917,102000,40,36000,46289,"CLASS 13H",LocomotiveType.STEAM_LOCOMOTIVE),
    USRA(1918, 90000, 40,19000,29993 ,"USRA", LocomotiveType.STEAM_LOCOMOTIVE),
    MIKADO(1919,133000,55,32000,51072,"MIKADO",LocomotiveType.STEAM_LOCOMOTIVE),
    BE_46_II(1920,610000,35,11000,14701,"BE 4/6 II",LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    CLASS_B12(1923,146000,71,14000,32407,"CLASS B12",LocomotiveType.STEAM_LOCOMOTIVE),
    EE_33(1923,47000,30,7000,11880,"EE 3/3",LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    CLASS_1045(1927,95000,40,6000,11636,"CLASS 1045",LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    MALLARD(1935,200000,126,19000,55136,"MALLARD",LocomotiveType.STEAM_LOCOMOTIVE),
    CG1(1935, 285000, 100, 19000,42721,"CG1", LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    F3A_B(1945,265000,85,16000,52150,"F3A+B",LocomotiveType.DIESEL_LOCOMOTIVE),
    ALCO_PA_1(1946,210000,90,16000,52800,"ALCO PA-1",LocomotiveType.DIESEL_LOCOMOTIVE),
    F9(1949, 337000, 110, 18000,63000,"F9", LocomotiveType.DIESEL_LOCOMOTIVE),
    GP9(1954,235000,71,11000,48028,"GP9",LocomotiveType.DIESEL_LOCOMOTIVE),
    E69(1955, 86000, 31,8000,12837, "E69", LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    GP18(1958,245000,83,15000,55131,"GP18",LocomotiveType.DIESEL_LOCOMOTIVE),
    V200(1959,160000,87,19000,53878,"V200",LocomotiveType.DIESEL_LOCOMOTIVE),
    PENN_E44(1960,370000,70,22000,37971,"Penn. E44",LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    CLASS_55_DELTIC(1961,480000,100,15000,52155,"Class 55 Deltic",LocomotiveType.DIESEL_LOCOMOTIVE),
    SHINKANSEN_BULLET(1966,650000,130,66000,66680,"Shinkansen Bullet",LocomotiveType.ELECTRICITY_LOCOMOTIVE),
    FP45(1968,366000,106,14000,65740,"FP45",LocomotiveType.DIESEL_LOCOMOTIVE),
    SD45(1972,280000,65,21000,36009,"SD45",LocomotiveType.DIESEL_LOCOMOTIVE),
    EUROSTAR(1994, 1600000, 175,65000,96135 ,"EUROSTAR", LocomotiveType.ELECTRICITY_LOCOMOTIVE);

    private final int startYear;
    private final double topSpeed;
    private final double acquisitionPrice;
    private final int maintenance;
    private final int fuelCoast;
    private final String name;
    private final LocomotiveType type;

    /**
     * Constructor for a locomotive model.
     *
     * @param startYear        the year the model became available
     * @param acquisitionPrice the cost to purchase the model
     * @param topSpeed         the top speed of the locomotive in mph
     * @param name             the name of the locomotive model
     * @param type             the type of the locomotive (Steam, Diesel, Electric)
     */
    LocomotiveModel(int startYear, double acquisitionPrice, double topSpeed,int maintenance, int fuelCoast, String name, LocomotiveType type) {
        this.acquisitionPrice = acquisitionPrice;
        this.startYear = startYear;
        this.topSpeed = topSpeed;
        this.maintenance = maintenance;
        this.fuelCoast = fuelCoast;
        this.name = name;
        this.type = type;

    }

    /**
     * Gets the top speed of the locomotive.
     *
     * @return the top speed in km/h
     */
    public double getTopSpeed() {
        return topSpeed;
    }

    /**
     * Gets the year the locomotive became available.
     *
     * @return the start year
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Gets the acquisition price of the locomotive.
     *
     * @return the price in simulation currency
     */
    public double getAcquisitionPrice() {
        return acquisitionPrice;
    }

    /**
     * Gets the name of the locomotive model.
     *
     * @return the name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the locomotive.
     *
     * @return the {@link LocomotiveType}
     */
    public LocomotiveType getType() {
        return type;
    }

    public int getMaintenance(){ return maintenance;}

    public  int getFuelCoast(){return  fuelCoast;}
}
