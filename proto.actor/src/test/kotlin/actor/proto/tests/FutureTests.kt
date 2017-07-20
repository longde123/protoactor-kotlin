package actor.proto.tests

import actor.proto.*
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertEquals

class FutureTests {
    @Test fun `given actor when requestAwait should return reply`() {
        val pid: PID = spawn(fromFunc {
            if (message is String) {
                respond("hey")
            }
        })
        runBlocking {
            val reply: Any = pid.requestAwait<Any>("hello")
            Assert.assertEquals("hey", reply)
        }
    }

    @Test fun `given actor when await context_requestAwait should get reply`() {
        val pid1: PID = spawn(fromFunc {
            if (message is String) {
                respond("hey")
            }
        })
        val pid2: PID = spawn(fromFunc {
            val m=message
            if (m is String) {
                val reply1 = requestAwait<String>(pid1, "")
                respond(m + reply1)
            }
        })
        runBlocking {
            val reply2 = pid2.requestAwait<String>("hello")
            assertEquals("hellohey", reply2)
        }
    }
}
