package midoriiro.audiofx.bluetooth.specifications

import kotlin.Int

class BluetoothGattServices {
  companion object {
    const val GENERIC_ACCESS: Int = 0x1800

    const val ALERT_NOTIFICATION_SERVICE: Int = 0x1811

    const val AUTOMATION_IO: Int = 0x1815

    const val BATTERY_SERVICE: Int = 0x180F

    const val BINARY_SENSOR: Int = 0x183B

    const val BLOOD_PRESSURE: Int = 0x1810

    const val BODY_COMPOSITION: Int = 0x181B

    const val BOND_MANAGEMENT_SERVICE: Int = 0x181E

    const val CONTINUOUS_GLUCOSE_MONITORING: Int = 0x181F

    const val CURRENT_TIME_SERVICE: Int = 0x1805

    const val CYCLING_POWER: Int = 0x1818

    const val CYCLING_SPEED_AND_CADENCE: Int = 0x1816

    const val DEVICE_INFORMATION: Int = 0x180A

    const val EMERGENCY_CONFIGURATION: Int = 0x183C

    const val ENVIRONMENTAL_SENSING: Int = 0x181A

    const val FITNESS_MACHINE: Int = 0x1826

    const val GENERIC_ATTRIBUTE: Int = 0x1801

    const val GLUCOSE: Int = 0x1808

    const val HEALTH_THERMOMETER: Int = 0x1809

    const val HEART_RATE: Int = 0x180D

    const val HTTP_PROXY: Int = 0x1823

    const val HUMAN_INTERFACE_DEVICE: Int = 0x1812

    const val IMMEDIATE_ALERT: Int = 0x1802

    const val INDOOR_POSITIONING: Int = 0x1821

    const val INSULIN_DELIVERY: Int = 0x183A

    const val INTERNET_PROTOCOL_SUPPORT_SERVICE: Int = 0x1820

    const val LINK_LOSS: Int = 0x1803

    const val LOCATION_AND_NAVIGATION: Int = 0x1819

    const val MESH_PROVISIONING_SERVICE: Int = 0x1827

    const val MESH_PROXY_SERVICE: Int = 0x1828

    const val NEXT_DST_CHANGE_SERVICE: Int = 0x1807

    const val OBJECT_TRANSFER_SERVICE: Int = 0x1825

    const val PHONE_ALERT_STATUS_SERVICE: Int = 0x180E

    const val PULSE_OXIMETER_SERVICE: Int = 0x1822

    const val RECONNECTION_CONFIGURATION: Int = 0x1829

    const val REFERENCE_TIME_UPDATE_SERVICE: Int = 0x1806

    const val RUNNING_SPEED_AND_CADENCE: Int = 0x1814

    const val SCAN_PARAMETERS: Int = 0x1813

    const val TRANSPORT_DISCOVERY: Int = 0x1824

    const val TX_POWER: Int = 0x1804

    const val USER_DATA: Int = 0x181C

    const val WEIGHT_SCALE: Int = 0x181D
  }
}
