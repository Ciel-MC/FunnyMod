plugins {
    id("fabric-loom") version "0.10.+"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

version = project.properties["mod_version"]!!
group = project.properties["maven_group"]!!

repositories {
    flatDir {
        dirs("/home/eric/PanelStudio/panelstudio-mc17/build/libs")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("http://172.105.231.97/releases")
    }
    maven {
        name = "meteor-maven-snapshots"
        url = uri("https://maven.meteordev.org/snapshots")
    }
    maven {
        name = "sonatype-oss-snapshots"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")

    fun includeModImpl(dependency: String) {
        modImplementation(include(dependency)!!)
    }

    //TCP networking
    includeModImpl("hk.eric.simpleTCP:SimpleTCP:${project.properties["simpleTCP_version"]}")
//    includeModImpl("hk.eric.simpleTCP:SimpleTCP:${project.properties["simpleTCP_version"]}")

    //Text API
    includeModImpl("net.kyori:adventure-platform-fabric:${project.properties["adventure_platform_version"]}")

    //Integrate with baritone
    includeModImpl("baritone:baritone-unoptimized-fabric:${project.properties["baritone_version"]}")

    //Integrate with viaversion
    //    modImplementation("com.viaversion.fabric:viafabric:${project.viafabric_version}")
    //    modImplementation("com.viaversion.viafabric:viafabric-mc118:${project.viafabric_version}")
    //    modImplementation("com.viaversion.viaversion:viaversion:${project.viaversion_version}")
    includeModImpl("org.yaml.snakeyml:snakeyaml:${project.properties["snakeyaml_version"]}")

    //For Json
    includeModImpl("com.fasterxml.jackson.core:jackson-core:${project.properties["jackson_version"]}")
    includeModImpl("com.fasterxml.jackson.core:jackson-annotations:${project.properties["jackson_version"]}")
    includeModImpl("com.fasterxml.jackson.core:jackson-databind:${project.properties["jackson_version"]}")

    //EricLib
    includeModImpl("hk.eric:EricLib:${project.properties["ericlib_version"]}")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    val targetJavaVersion = 16
    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }

//    withType<KotlinCompile>().configureEach {
//        kotlinOptions {
//            jvmTarget = "17"
//        }
//    }

    java {
        withSourcesJar()
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${archiveBaseName}" }
        }
    }

    test {
        useJUnitPlatform()
    }
}