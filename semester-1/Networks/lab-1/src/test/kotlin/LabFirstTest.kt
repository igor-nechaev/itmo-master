import com.example.exmple.labFirst
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LabFirstTest {

    @Test
    fun labFirstTest() {
        val messages = listOf(
            listOf("DOG", "CAT", "HAM", "SUN"),
            listOf("SUN", "HAM", "CAT", "DOG"),
        )
        val expected = listOf(
            listOf("DOG", "CAT", "HAM", "SUN"),
            listOf("SUN", "HAM", "CAT", "DOG"),
        )

        val receivedMessages = messages.map { messages ->
            labFirst(
                n = messages.size,
                messages = messages
            )
        }

        expected.forEachIndexed { index, expected ->
            assertEquals(expected, receivedMessages[index])
        }
    }
}
