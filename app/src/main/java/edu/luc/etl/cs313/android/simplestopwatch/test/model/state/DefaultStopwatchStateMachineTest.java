package edu.luc.etl.cs313.android.simplestopwatch.test.model.state;

/**
 * Concrete testcase subclass for the default stopwatch state machine
 * implementation.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html

public class DefaultStopwatchStateMachineTest extends AbstractStopwatchStateMachineTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setModel(new DefaultStopwatchStateMachine(getDependency(), getDependency()));
    }

    @After
    public void tearDown() {
        setModel(null);
        super.tearDown();
    }
}*/
