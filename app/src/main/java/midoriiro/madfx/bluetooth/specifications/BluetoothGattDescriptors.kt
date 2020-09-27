package midoriiro.madfx.bluetooth.specifications

import kotlin.Int

class BluetoothGattDescriptors {
  companion object {
    const val CHARACTERISTIC_AGGREGATE_FORMAT: Int = 0x2905

    const val CHARACTERISTIC_EXTENDED_PROPERTIES: Int = 0x2900

    const val CHARACTERISTIC_PRESENTATION_FORMAT: Int = 0x2904

    const val CHARACTERISTIC_USER_DESCRIPTION: Int = 0x2901

    const val CLIENT_CHARACTERISTIC_CONFIGURATION: Int = 0x2902

    const val ENVIRONMENTAL_SENSING_CONFIGURATION: Int = 0x290B

    const val ENVIRONMENTAL_SENSING_MEASUREMENT: Int = 0x290C

    const val ENVIRONMENTAL_SENSING_TRIGGER_SETTING: Int = 0x290D

    const val EXTERNAL_REPORT_REFERENCE: Int = 0x2907

    const val NUMBER_OF_DIGITALS: Int = 0x2909

    const val REPORT_REFERENCE: Int = 0x2908

    const val SERVER_CHARACTERISTIC_CONFIGURATION: Int = 0x2903

    const val TIME_TRIGGER_SETTING: Int = 0x290E

    const val VALID_RANGE: Int = 0x2906

    const val VALUE_TRIGGER_SETTING: Int = 0x290A
  }
}
