package alice.tucson.examples.spawnedWorkers;

import java.math.BigInteger;
import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.tucson.api.AbstractSpawnActivity;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * 
 * 
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class SpawnedWorkingActivity extends AbstractSpawnActivity {
    private static final long serialVersionUID = -4459068799410719933L;

    /*
     * 
     */
    @Override
    public void doActivity() {
        try {
            /*
             * Jobs collection phase.
             */
            final LogicTuple templ = LogicTuple
                    .parse("fact(master(M),num(N),reqID(R))");
            this.log("Waiting for jobs...");
            /*
             * Watch out: it's a suspensive primitive! If no jobs are available
             * we are stuck!
             */
            final LogicTuple job = this.in(templ);
            this.log("Found job: " + job.toString());
            /*
             * Computation phase.
             */
            final BigInteger bigNum = this.computeFactorial(job.getArg("num")
                    .getArg(0));
            /*
             * Result submission phase.
             */
            final LogicTuple res = LogicTuple.parse("res(" + "master("
                    + job.getArg("master").getArg(0) + ")," + "fact("
                    + bigNum.toString() + ")," + "reqID("
                    + job.getArg("reqID").getArg(0) + ")" + ")");
            this.log("Putting result: " + res.toString());
            this.out(res);
        } catch (final InvalidTupleException e) {
            this.log("ERROR: Tuple is not an admissible Prolog term!");
            e.printStackTrace();
        } catch (final InvalidOperationException e) {
            this.log("ERROR: No tuple arguments to retrieve!");
            e.printStackTrace();
        }
    }

    private BigInteger computeFactorial(final TupleArgument varValue) {
        try {
            final int num = varValue.intValue();
            this.log("Computing factorial for: " + num + "...");
            return this.factorial(num);
        } catch (final InvalidOperationException e) {
            this.log("Not an Integer value, killing myself...");
            return new BigInteger("-1");
        }
    }

    private BigInteger factorial(final int num) {
        if (num == 0) {
            return BigInteger.ONE;
        }
        return new BigInteger(String.valueOf(num)).multiply(this
                .factorial(num - 1));
    }
}
