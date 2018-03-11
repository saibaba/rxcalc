package calc
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.regex.Matcher
import java.util.regex.Pattern

class ReactiveCalculator(a:Int, b:Int) {

    internal val subjectAdd: Subject<Pair<Int, Int>> = PublishSubject.create()

    internal val subjectCalc:Subject<ReactiveCalculator> = PublishSubject.create()

    internal var nums:Pair<Int, Int>  = Pair(0,0)

    init {
        nums = Pair(a, b)

        subjectAdd.map({it.first + it.second}).subscribe({println("Add = $it")})

        subjectCalc.subscribe({
            with(it) {
                calculateAddition()
                calculateSubtraction()
            }
        })

        subjectCalc.onNext(this)
    }

    fun calculateAddition() {
        subjectAdd.onNext(nums)
    }

    private inline fun calculateSubtraction(): Int {
        val result = nums.first - nums.second
        println("Subtract = $result")
        return result
    }

    fun modifyNumbers(a:Int = nums.first, b:Int = nums.second) {
        nums = Pair(a, b)
        subjectCalc.onNext(this)
    }

    fun handleInput(inputLine:String?) {
        if (!inputLine.equals("exit")) {
            val pattern: Pattern = Pattern.compile("([a|b])(?:\\s)?=(?:\\s)?(\\d*)")

            var a:Int?  = null
            var b: Int? = null

            val matcher : Matcher = pattern.matcher(inputLine)

            if (matcher.matches() && matcher.group(1) != null
                    && matcher.group(2) != null) {
                if(matcher.group(1).toLowerCase().equals("a")){
                    a = matcher.group(2).toInt()
                } else if(matcher.group(1).toLowerCase().equals("b")){
                    b = matcher.group(2).toInt()
                }
            }
            when {
                a != null && b != null -> modifyNumbers(a, b)
                a != null -> modifyNumbers(a = a)
                b != null -> modifyNumbers(b = b)
                else -> println("Invalid Input")
            }

        }
    }


}

fun main(args: Array<String>) {
    println("Initial Out put with a = 15, b = 10")
    var calculator:ReactiveCalculator = ReactiveCalculator(15,10)
    println("Enter a = <number> or b = <number> in separate lines\nexit to exit the program")
            var line:String?
    do {
        line = readLine();
        calculator.handleInput(line)
    } while (line!= null && !line.toLowerCase().contains("exit"))
}

