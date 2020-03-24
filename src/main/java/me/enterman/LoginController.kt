package me.enterman

import tornadofx.Controller
import tornadofx.runLater
import java.net.URLClassLoader
import java.time.Instant
import java.util.*
import kotlin.system.exitProcess

class LoginController : Controller() {
    val loginScreen: LoginScreen by inject()
    //val secureScreen: SecureScreen by inject()

    fun init() {
        showLoginScreen("请登录")
    }

    private fun showLoginScreen(message: String, shake: Boolean = false) {
        loginScreen.title = message
        //secureScreen.replaceWith(loginScreen, sizeToScene = true, centerOnScreen = true)
        runLater {
            if (shake) loginScreen.shakeStage()
        }
    }

    fun showSecureScreen() {
        //loginScreen.replaceWith(secureScreen, sizeToScene = true, centerOnScreen = true)
    }

    fun tryLogin(username: String, password: String, remember: Boolean) {
        runAsync {
            loginStatus(Encryption["api.arithmo.tk:8080?fuck=" + Encryption.encrypt(listOf(username,password,Encryption.hid(), Instant.now().epochSecond).joinToString(","),"")])
        } ui { status ->

            if (status.loggedIn) {
                // TODO
            } else {
                showLoginScreen("密码错误", true)
                loginScreen.clear()
            }
        }
    }


}
class loginStatus(response:String){
    val loggedIn:Boolean
    val fuck:String = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKyDkUeOBidd3fKaTuK/ziShK7uADXsHXJcqvHEsUow0z4hHyhOVutYq/kuK4/sk665tIyOeRX+w9C4kUakwllvT+uOFeh0hkrThUfp4PdKTRH3EnN2CPlLB16rKuTx24LvE6hQ6BS4iZFAlRY7W40WoWoCGBko0kJdYkvxLH5bbAgMBAAECgYAMIo0bnW/tYPr1z8jwxlE33mZ3IADSwKfQsl7jDx0XY7edfH9IdWXZHtkYc0KqLeSuNlscbEuzOb1TaZac8iABY8sBRHFOyz2GW1Cg48GxPan8+liZO+6hgFgIFKHR55D6dMIeQw+om9V2PuHSizG2E+rh3oSfsyEiV2vf/UzUYQJBAO/WkzP2dZ8OgwKMuAAUKcGj5qKDcTCupY7b8Ky/DBks8qe/rWRNg7jgV68Y5MLK818ExANGrpLQilo64KascVMCQQC4I5am48QiNQeCM0/GinNu72jNKOJZV9fnfIlS2UjVfXl/F1pw/OBOTRnfWOY319WVXBQsM/1hgnCyeedgp+tZAkAbmNAlWYWdQLk4cOK3N5q6alaeeotqs53fO1WNOwp0VySwwrB76yNuAXp1bmLcOygX1d+MFPT/dbmFumKhKxR7AkBQBG8QAMQXVVH4+1E8Zqe42mKBXLXXuzuARXbrQK7MzyS/3KcZmFnkzM5kZx5LwAf5SrUdbE48VFGizSSFtRKpAkBN3c1kuW8MGuyjrdfH9A6h/q57nz0fsrSEZlIbIgl+m/otXmxXBy2mBlX6OcVK1f2YbnynTAt3n7DUxOokALvt"
    val status:String
    val bytes:ByteArray
    init {
        val s = Encryption.decrypt(response,fuck)
        val strings = s.split(",")
        loggedIn = strings[0].toBoolean()
        status = strings[1]
        bytes = Base64.getDecoder().decode(strings[2])
        if(Instant.now().epochSecond - strings[3].toLong() > 10)
            exitProcess(-1)
        URLClassLoader
        //loggedIn =
    }
}