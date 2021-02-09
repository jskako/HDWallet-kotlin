import java.security.SecureRandom

class PasswordManager {

    private val letters : String = "abcdefghijklmnopqrstuvwxyz"
    private val uppercaseLetters : String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numbers : String = "0123456789"
    private val special : String = "@#=+!£$%&?"

    fun generatePassword(isWithLetters: Boolean,
                         isWithUppercase: Boolean,
                         isWithNumbers: Boolean,
                         isWithSpecial: Boolean,
                         length: Int) : String {

        var result = ""
        var i = 0

        if(isWithLetters){ result += this.letters }
        if(isWithUppercase){ result += this.uppercaseLetters }
        if(isWithNumbers){ result += this.numbers }
        if(isWithSpecial){ result += this.special }

        val rnd = SecureRandom.getInstance("SHA1PRNG")
        val sb = StringBuilder(length)

        while (i < length) {
            val randomInt : Int = rnd.nextInt(result.length)
            sb.append(result[randomInt])
            i++
        }

        return sb.toString()
    }

}