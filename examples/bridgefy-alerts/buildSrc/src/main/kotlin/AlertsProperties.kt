import java.io.File
import java.io.FileInputStream
import java.lang.IllegalStateException
import java.util.Properties
import org.apache.tools.ant.taskdefs.condition.Os

object AlertsProperties {
    fun readAlertsProperties(name: String): Properties {
        val alertsFileName = "$name.properties"
        val alertsPropertiesFileDirname =
            if (Os.isFamily(Os.FAMILY_WINDOWS)) "${System.getProperty("user.home")}/.app-version-codes"
            else "${System.getProperty("user.home")}/.app-version-codes"

        val propertiesDir = File(alertsPropertiesFileDirname)
        if (!propertiesDir.exists() && !propertiesDir.mkdirs()) {
            throw IllegalStateException("Cannot create versions directory [${propertiesDir.absolutePath}]")
        }

        val alertsPropsFile = File(propertiesDir, alertsFileName)
        val alertsProps = Properties()
        if (alertsPropsFile.canRead()) {
            val reader = FileInputStream(alertsPropsFile)
            alertsProps.load(reader)
            reader.close()
        } else {

            //throw IllegalStateException("Failed to read bridgefy properties file [${bridgefyPropsFile.absolutePath}]")
            val localProperties = File("gradle.properties")
            val reader = FileInputStream(localProperties)
            alertsProps.load(reader)
            reader.close()
        }
        return alertsProps
    }
}