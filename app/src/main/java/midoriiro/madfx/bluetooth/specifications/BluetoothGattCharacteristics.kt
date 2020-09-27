package midoriiro.madfx.bluetooth.specifications

class BluetoothGattCharacteristics {
  companion object {
    const val AEROBIC_HEART_RATE_LOWER_LIMIT: Int = 0x2A7E

    const val AEROBIC_HEART_RATE_UPPER_LIMIT: Int = 0x2A84

    const val AEROBIC_THRESHOLD: Int = 0x2A7F

    const val AGE: Int = 0x2A80

    const val AGGREGATE: Int = 0x2A5A

    const val ALERT_CATEGORY_ID: Int = 0x2A43

    const val ALERT_CATEGORY_ID_BIT_MASK: Int = 0x2A42

    const val ALERT_LEVEL: Int = 0x2A06

    const val ALERT_NOTIFICATION_CONTROL_POINT: Int = 0x2A44

    const val ALERT_STATUS: Int = 0x2A3F

    const val ALTITUDE: Int = 0x2AB3

    const val ANAEROBIC_HEART_RATE_LOWER_LIMIT: Int = 0x2A81

    const val ANAEROBIC_HEART_RATE_UPPER_LIMIT: Int = 0x2A82

    const val ANAEROBIC_THRESHOLD: Int = 0x2A83

    const val ANALOG: Int = 0x2A58

    const val ANALOG_OUTPUT: Int = 0x2A59

    const val APPARENT_WIND_DIRECTION: Int = 0x2A73

    const val APPARENT_WIND_SPEED: Int = 0x2A72

    const val APPEARANCE: Int = 0x2A01

    const val BAROMETRIC_PRESSURE_TREND: Int = 0x2AA3

    const val BATTERY_LEVEL: Int = 0x2A19

    const val BATTERY_LEVEL_STATE: Int = 0x2A1B

    const val BATTERY_POWER_STATE: Int = 0x2A1A

    const val BLOOD_PRESSURE_FEATURE: Int = 0x2A49

    const val BLOOD_PRESSURE_MEASUREMENT: Int = 0x2A35

    const val BODY_COMPOSITION_FEATURE: Int = 0x2A9B

    const val BODY_COMPOSITION_MEASUREMENT: Int = 0x2A9C

    const val BODY_SENSOR_LOCATION: Int = 0x2A38

    const val BOND_MANAGEMENT_CONTROL_POINT: Int = 0x2AA4

    const val BOND_MANAGEMENT_FEATURES: Int = 0x2AA5

    const val BOOT_KEYBOARD_INPUT_REPORT: Int = 0x2A22

    const val BOOT_KEYBOARD_OUTPUT_REPORT: Int = 0x2A32

    const val BOOT_MOUSE_INPUT_REPORT: Int = 0x2A33

    const val BSS_CONTROL_POINT: Int = 0x2B2B

    const val BSS_RESPONSE: Int = 0x2B2C

    const val CGM_FEATURE: Int = 0x2AA8

    const val CGM_MEASUREMENT: Int = 0x2AA7

    const val CGM_SESSION_RUN_TIME: Int = 0x2AAB

    const val CGM_SESSION_START_TIME: Int = 0x2AAA

    const val CGM_SPECIFIC_OPS_CONTROL_POINT: Int = 0x2AAC

    const val CGM_STATUS: Int = 0x2AA9

    const val CLIENT_SUPPORTED_FEATURES: Int = 0x2B29

    const val CROSS_TRAINER_DATA: Int = 0x2ACE

    const val CSC_FEATURE: Int = 0x2A5C

    const val CSC_MEASUREMENT: Int = 0x2A5B

    const val CURRENT_TIME: Int = 0x2A2B

    const val CYCLING_POWER_CONTROL_POINT: Int = 0x2A66

    const val CYCLING_POWER_FEATURE: Int = 0x2A65

    const val CYCLING_POWER_MEASUREMENT: Int = 0x2A63

    const val CYCLING_POWER_VECTOR: Int = 0x2A64

    const val DATABASE_CHANGE_INCREMENT: Int = 0x2A99

    const val DATABASE_HASH: Int = 0x2B2A

    const val DATE_OF_BIRTH: Int = 0x2A85

    const val DATE_OF_THRESHOLD_ASSESSMENT: Int = 0x2A86

    const val DATE_TIME: Int = 0x2A08

    const val DATE_UTC: Int = 0x2AED

    const val DAY_DATE_TIME: Int = 0x2A0A

    const val DAY_OF_WEEK: Int = 0x2A09

    const val DESCRIPTOR_VALUE_CHANGED: Int = 0x2A7D

    const val DEW_POINT: Int = 0x2A7B

    const val DIGITAL: Int = 0x2A56

    const val DIGITAL_OUTPUT: Int = 0x2A57

    const val DST_OFFSET: Int = 0x2A0D

    const val ELEVATION: Int = 0x2A6C

    const val EMAIL_ADDRESS: Int = 0x2A87

    const val EMERGENCY_ID: Int = 0x2B2D

    const val EMERGENCY_TEXT: Int = 0x2B2E

    const val EXACT_TIME_100: Int = 0x2A0B

    const val EXACT_TIME_256: Int = 0x2A0C

    const val FAT_BURN_HEART_RATE_LOWER_LIMIT: Int = 0x2A88

    const val FAT_BURN_HEART_RATE_UPPER_LIMIT: Int = 0x2A89

    const val FIRMWARE_REVISION_STRING: Int = 0x2A26

    const val FIRST_NAME: Int = 0x2A8A

    const val FITNESS_MACHINE_CONTROL_POINT: Int = 0x2AD9

    const val FITNESS_MACHINE_FEATURE: Int = 0x2ACC

    const val FITNESS_MACHINE_STATUS: Int = 0x2ADA

    const val FIVE_ZONE_HEART_RATE_LIMITS: Int = 0x2A8B

    const val FLOOR_NUMBER: Int = 0x2AB2

    const val CENTRAL_ADDRESS_RESOLUTION: Int = 0x2AA6

    const val DEVICE_NAME: Int = 0x2A00

    const val PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS: Int = 0x2A04

    const val PERIPHERAL_PRIVACY_FLAG: Int = 0x2A02

    const val RECONNECTION_ADDRESS: Int = 0x2A03

    const val SERVICE_CHANGED: Int = 0x2A05

    const val GENDER: Int = 0x2A8C

    const val GLUCOSE_FEATURE: Int = 0x2A51

    const val GLUCOSE_MEASUREMENT: Int = 0x2A18

    const val GLUCOSE_MEASUREMENT_CONTEXT: Int = 0x2A34

    const val GUST_FACTOR: Int = 0x2A74

    const val HARDWARE_REVISION_STRING: Int = 0x2A27

    const val HEART_RATE_CONTROL_POINT: Int = 0x2A39

    const val HEART_RATE_MAX: Int = 0x2A8D

    const val HEART_RATE_MEASUREMENT: Int = 0x2A37

    const val HEAT_INDEX: Int = 0x2A7A

    const val HEIGHT: Int = 0x2A8E

    const val HID_CONTROL_POINT: Int = 0x2A4C

    const val HID_INFORMATION: Int = 0x2A4A

    const val HIP_CIRCUMFERENCE: Int = 0x2A8F

    const val HTTP_CONTROL_POINT: Int = 0x2ABA

    const val HTTP_ENTITY_BODY: Int = 0x2AB9

    const val HTTP_HEADERS: Int = 0x2AB7

    const val HTTP_STATUS_CODE: Int = 0x2AB8

    const val HTTPS_SECURITY: Int = 0x2ABB

    const val HUMIDITY: Int = 0x2A6F

    const val IDD_ANNUNCIATION_STATUS: Int = 0x2B22

    const val IDD_COMMAND_CONTROL_POINT: Int = 0x2B25

    const val IDD_COMMAND_DATA: Int = 0x2B26

    const val IDD_FEATURES: Int = 0x2B23

    const val IDD_HISTORY_DATA: Int = 0x2B28

    const val IDD_RECORD_ACCESS_CONTROL_POINT: Int = 0x2B27

    const val IDD_STATUS: Int = 0x2B21

    const val IDD_STATUS_CHANGED: Int = 0x2B20

    const val IDD_STATUS_READER_CONTROL_POINT: Int = 0x2B24

    const val `IEEE_11073-20601_REGULATORY_CERTIFICATION_DATA_LIST`: Int = 0x2A2A

    const val INDOOR_BIKE_DATA: Int = 0x2AD2

    const val INDOOR_POSITIONING_CONFIGURATION: Int = 0x2AAD

    const val INTERMEDIATE_CUFF_PRESSURE: Int = 0x2A36

    const val INTERMEDIATE_TEMPERATURE: Int = 0x2A1E

    const val IRRADIANCE: Int = 0x2A77

    const val LANGUAGE: Int = 0x2AA2

    const val LAST_NAME: Int = 0x2A90

    const val LATITUDE: Int = 0x2AAE

    const val LN_CONTROL_POINT: Int = 0x2A6B

    const val LN_FEATURE: Int = 0x2A6A

    const val LOCAL_EAST_COORDINATE: Int = 0x2AB1

    const val LOCAL_NORTH_COORDINATE: Int = 0x2AB0

    const val LOCAL_TIME_INFORMATION: Int = 0x2A0F

    const val LOCATION_AND_SPEED_CHARACTERISTIC: Int = 0x2A67

    const val LOCATION_NAME: Int = 0x2AB5

    const val LONGITUDE: Int = 0x2AAF

    const val MAGNETIC_DECLINATION: Int = 0x2A2C

    const val `MAGNETIC_FLUX_DENSITY_–_2D`: Int = 0x2AA0

    const val `MAGNETIC_FLUX_DENSITY_–_3D`: Int = 0x2AA1

    const val MANUFACTURER_NAME_STRING: Int = 0x2A29

    const val MAXIMUM_RECOMMENDED_HEART_RATE: Int = 0x2A91

    const val MEASUREMENT_INTERVAL: Int = 0x2A21

    const val MESH_PROVISIONING_DATA_IN: Int = 0x2ADB

    const val MESH_PROVISIONING_DATA_OUT: Int = 0X2ADC

    const val MESH_PROXY_DATA_IN: Int = 0X2ADD

    const val MESH_PROXY_DATA_OUT: Int = 0X2ADE

    const val MODEL_NUMBER_STRING: Int = 0x2A24

    const val NAVIGATION: Int = 0x2A68

    const val NETWORK_AVAILABILITY: Int = 0x2A3E

    const val NEW_ALERT: Int = 0x2A46

    const val OBJECT_ACTION_CONTROL_POINT: Int = 0x2AC5

    const val OBJECT_CHANGED: Int = 0x2AC8

    const val `OBJECT_FIRST-CREATED`: Int = 0x2AC1

    const val OBJECT_ID: Int = 0x2AC3

    const val `OBJECT_LAST-MODIFIED`: Int = 0x2AC2

    const val OBJECT_LIST_CONTROL_POINT: Int = 0x2AC6

    const val OBJECT_LIST_FILTER: Int = 0x2AC7

    const val OBJECT_NAME: Int = 0x2ABE

    const val OBJECT_PROPERTIES: Int = 0x2AC4

    const val OBJECT_SIZE: Int = 0x2AC0

    const val OBJECT_TYPE: Int = 0x2ABF

    const val OTS_FEATURE: Int = 0x2ABD

    const val PLX_CONTINUOUS_MEASUREMENT_CHARACTERISTIC: Int = 0x2A5F

    const val PLX_FEATURES: Int = 0x2A60

    const val `PLX_SPOT-CHECK_MEASUREMENT`: Int = 0x2A5E

    const val PNP_ID: Int = 0x2A50

    const val POLLEN_CONCENTRATION: Int = 0x2A75

    const val POSITION_2D: Int = 0x2A2F

    const val POSITION_3D: Int = 0x2A30

    const val POSITION_QUALITY: Int = 0x2A69

    const val PRESSURE: Int = 0x2A6D

    const val PROTOCOL_MODE: Int = 0x2A4E

    const val PULSE_OXIMETRY_CONTROL_POINT: Int = 0x2A62

    const val RAINFALL: Int = 0x2A78

    const val RC_FEATURE: Int = 0x2B1D

    const val RC_SETTINGS: Int = 0x2B1E

    const val RECONNECTION_CONFIGURATION_CONTROL_POINT: Int = 0x2B1F

    const val RECORD_ACCESS_CONTROL_POINT: Int = 0x2A52

    const val REFERENCE_TIME_INFORMATION: Int = 0x2A14

    const val REGISTERED_USER_CHARACTERISTIC: Int = 0x2B37

    const val REMOVABLE: Int = 0x2A3A

    const val REPORT: Int = 0x2A4D

    const val REPORT_MAP: Int = 0x2A4B

    const val RESOLVABLE_PRIVATE_ADDRESS_ONLY: Int = 0x2AC9

    const val RESTING_HEART_RATE: Int = 0x2A92

    const val RINGER_CONTROL_POINT: Int = 0x2A40

    const val RINGER_SETTING: Int = 0x2A41

    const val ROWER_DATA: Int = 0x2AD1

    const val RSC_FEATURE: Int = 0x2A54

    const val RSC_MEASUREMENT: Int = 0x2A53

    const val SC_CONTROL_POINT: Int = 0x2A55

    const val SCAN_INTERVAL_WINDOW: Int = 0x2A4F

    const val SCAN_REFRESH: Int = 0x2A31

    const val SCIENTIFIC_TEMPERATURE_CELSIUS: Int = 0x2A3C

    const val SECONDARY_TIME_ZONE: Int = 0x2A10

    const val SENSOR_LOCATION: Int = 0x2A5D

    const val SERIAL_NUMBER_STRING: Int = 0x2A25

    const val SERVER_SUPPORTED_FEATURES: Int = 0x2B3A

    const val SERVICE_REQUIRED: Int = 0x2A3B

    const val SOFTWARE_REVISION_STRING: Int = 0x2A28

    const val SPORT_TYPE_FOR_AEROBIC_AND_ANAEROBIC_THRESHOLDS: Int = 0x2A93

    const val STAIR_CLIMBER_DATA: Int = 0x2AD0

    const val STEP_CLIMBER_DATA: Int = 0x2ACF

    const val STRING: Int = 0x2A3D

    const val SUPPORTED_HEART_RATE_RANGE: Int = 0x2AD7

    const val SUPPORTED_INCLINATION_RANGE: Int = 0x2AD5

    const val SUPPORTED_NEW_ALERT_CATEGORY: Int = 0x2A47

    const val SUPPORTED_POWER_RANGE: Int = 0x2AD8

    const val SUPPORTED_RESISTANCE_LEVEL_RANGE: Int = 0x2AD6

    const val SUPPORTED_SPEED_RANGE: Int = 0x2AD4

    const val SUPPORTED_UNREAD_ALERT_CATEGORY: Int = 0x2A48

    const val SYSTEM_ID: Int = 0x2A23

    const val TDS_CONTROL_POINT: Int = 0x2ABC

    const val TEMPERATURE: Int = 0x2A6E

    const val TEMPERATURE_CELSIUS: Int = 0x2A1F

    const val TEMPERATURE_FAHRENHEIT: Int = 0x2A20

    const val TEMPERATURE_MEASUREMENT: Int = 0x2A1C

    const val TEMPERATURE_TYPE: Int = 0x2A1D

    const val THREE_ZONE_HEART_RATE_LIMITS: Int = 0x2A94

    const val TIME_ACCURACY: Int = 0x2A12

    const val TIME_BROADCAST: Int = 0x2A15

    const val TIME_SOURCE: Int = 0x2A13

    const val TIME_UPDATE_CONTROL_POINT: Int = 0x2A16

    const val TIME_UPDATE_STATE: Int = 0x2A17

    const val TIME_WITH_DST: Int = 0x2A11

    const val TIME_ZONE: Int = 0x2A0E

    const val TRAINING_STATUS: Int = 0x2AD3

    const val TREADMILL_DATA: Int = 0x2ACD

    const val TRUE_WIND_DIRECTION: Int = 0x2A71

    const val TRUE_WIND_SPEED: Int = 0x2A70

    const val TWO_ZONE_HEART_RATE_LIMIT: Int = 0x2A95

    const val TX_POWER_LEVEL: Int = 0x2A07

    const val UNCERTAINTY: Int = 0x2AB4

    const val UNREAD_ALERT_STATUS: Int = 0x2A45

    const val URI: Int = 0x2AB6

    const val USER_CONTROL_POINT: Int = 0x2A9F

    const val USER_INDEX: Int = 0x2A9A

    const val UV_INDEX: Int = 0x2A76

    const val VO2_MAX: Int = 0x2A96

    const val WAIST_CIRCUMFERENCE: Int = 0x2A97

    const val WEIGHT: Int = 0x2A98

    const val WEIGHT_MEASUREMENT: Int = 0x2A9D

    const val WEIGHT_SCALE_FEATURE: Int = 0x2A9E

    const val WIND_CHILL: Int = 0x2A79
  }
}
