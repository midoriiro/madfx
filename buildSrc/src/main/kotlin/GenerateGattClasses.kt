
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.jsoup.Jsoup
import java.nio.file.Paths

open class GenerateGattClasses: DefaultTask()
{
	private val SERVICE_CLASS_NAME = "BluetoothGattServices"
	private val CHARACTERISTICS_CLASS_NAME = "BluetoothGattCharacteristics"
	private val DESCRIPTORS_CLASS_NAME = "BluetoothGattDescriptors"
	private val DECLARATIONS_CLASS_NAME = "BluetoothGattDeclarations"
	private val SERVICES_URL = "https://www.bluetooth.com/specifications/gatt/services/"
	private val CHARACTERISTICS_URL = "https://www.bluetooth.com/specifications/gatt/characteristics/"
	private val DESCRIPTORS_URL = "https://www.bluetooth.com/specifications/gatt/descriptors/"
	private val DECLARATIONS_URL = "https://www.bluetooth.com/specifications/gatt/declarations/"
	
	@Input
	lateinit var packageName: String
	
	@Input
	lateinit var outputPath: String
	
	@TaskAction
	fun generate()
	{
		val files: MutableList<FileSpec> = mutableListOf()
		files.add(this.getFileSpec(this.SERVICES_URL, this.SERVICE_CLASS_NAME))
		files.add(this.getFileSpec(this.CHARACTERISTICS_URL, this.CHARACTERISTICS_CLASS_NAME))
		files.add(this.getFileSpec(this.DESCRIPTORS_URL, this.DESCRIPTORS_CLASS_NAME))
		files.add(this.getFileSpec(this.DECLARATIONS_URL, this.DECLARATIONS_CLASS_NAME))
		
		val projectPath = this.project.file(".")
		val path = Paths.get(projectPath.absolutePath, this.outputPath)
		files.forEach { it.writeTo(path) }
	}
	
	private fun getFileSpec(url: String, className: String): FileSpec
	{
		val companion = TypeSpec.companionObjectBuilder()
		val document = Jsoup.connect(url).get()
		val rows = document.select(".table > tbody > tr")
		for(row in rows)
		{
			val data = row.select("td")
			val name = data[0].text()
			val assignedNumber = data[2].text()
			companion.addProperty(
				PropertySpec
					.builder(this.format(name), Int::class)
					.addModifiers(KModifier.CONST, KModifier.PUBLIC)
					.initializer("%L", assignedNumber)
					.build()
			)
		}
		return FileSpec
			.builder(this.packageName, className)
			.addType(
				TypeSpec.classBuilder(className)
					.addType(companion.build())
					.build()
			)
			.build()
	}
	
	private fun format(name: String): String
	{
		return name
			.split(" ")
			.map { it.toUpperCase() }
			.joinToString("_")
	}
}