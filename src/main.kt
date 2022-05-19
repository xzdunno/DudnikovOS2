import java.lang.Math.pow
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.system.measureTimeMillis
var hash:String=""
class Decrypt(
    private val threadNum:Int,
    private val start:Int,
    private val finish:Int
):Thread(){
    var isEqual=false
    var password:String=""
    override fun run() {
        val time= measureTimeMillis {
            for(i in start until finish){
                if (isEqual) break
                password=to26(i)
                if(password.hash()==hash){
                    println("Поток $threadNum нашёл значение")
                    println("Искомое значение $password")
                    isEqual=true
                }
            }

        }
println("Поток $threadNum завершил выполнение за ${time/1000} секунд")
        interrupt()
    }
}
fun main(){
    println("Введите Хэш")
    hash= readLine().toString()
    println("Введите количество потоков")
    val countThreads= readLine().toString().toInt()
    val all=pow(26.0,5.0).toInt()
    val forOne=all/countThreads
    for (i in 0..countThreads-1){
        var thr:Decrypt
        if (i==countThreads-1) thr=Decrypt(i+1,i*forOne,(i+1)*forOne+all%countThreads+1)
        else thr=Decrypt(i+1,i*forOne,(i+1)*forOne)
        thr.start()
    }
}
fun String.hash() = try {
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(this.toByteArray())

    val sb = StringBuilder()
    hash.forEach {
        sb.append(String.format("%02x", it))
    }

    sb.toString()
} catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
    null
}
fun to26(n:Int):String{
    var str=""
    for (i in 0..4){
        str=(n/ pow(26.0,i.toDouble()).toInt()%26+97).toChar()+str
    }
    return str
}
