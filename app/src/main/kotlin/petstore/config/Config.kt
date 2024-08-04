package petstore.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addEnvironmentSource
import com.sksamuel.hoplite.addFileSource
import com.sksamuel.hoplite.addResourceSource
import java.io.File

data class Config(
    val datasource: RdbmsConfig,
) {
    companion object {

        private const val CONFIG = "petstore"

        fun load(): Config {

            fun ConfigLoaderBuilder.addOptionalFileSource(configDir: String, fileName: String) =
                addFileSource(File(configDir, fileName), optional = true, allowEmpty = true)

            val workingDirConfig = File(System.getProperty("user.dir"), "config").canonicalPath
            val userDir = File(System.getProperty("user.dir")).canonicalPath

            return ConfigLoaderBuilder.default()
                .addResourceSource("$CONFIG.yaml")
                .addEnvironmentSource()
                .addOptionalFileSource(userDir, ".$CONFIG.yaml")
                .addOptionalFileSource(workingDirConfig, "$CONFIG.yaml")
                .build()
                .loadConfigOrThrow<Config>()
        }
    }
}
