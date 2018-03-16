package alice.tuple.java.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import alice.tuple.java.api.JArg;
import alice.tuple.java.api.JArgType;
import alice.tuple.java.api.JTuple;
import alice.tuple.java.api.JTupleTemplate;
import alice.tuple.java.api.JVal;
import alice.tuple.java.api.JVar;
import alice.tuple.java.exceptions.InvalidJValException;
import alice.tuple.java.exceptions.InvalidJVarException;
import alice.tuple.logic.LogicMatchingEngine;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArguments;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public final class JTuplesEngine {

    /**
     * @param lt the tuProlog LogicTuple to be checked
     * @return wether the given tuProlog LogicTuple represents a template
     * (namely, wether it contains any variable)
     * @throws InvalidTupleException if the given tuProlog LogicTuple is not convertible into a
     *                               JTuple (too much expressive)
     */
    public static boolean isTemplate(final LogicTuple lt)
            throws InvalidTupleException {
        if ("javat".equals(lt.getName())) {
            final int a = lt.getArity();
            for (int i = 0; i < a; i++) {
                final TupleArgument ta = lt.getArg(i);
                if (ta.getArity() == 0) {
                    if (ta.isVar()) {
                        return true;
                    }
                    throw new InvalidTupleException();
                } else if (ta.getArity() == 1) {
                    final TupleArgument ta2 = ta.getArg(0);
                    if (ta2.isVar()) {
                        return true;
                    }
                }
            }
            return false;
        }
        throw new InvalidTupleException("Error occurred while converting '"
                + lt.toString() + "' into JTuple");
    }

    /**
     * Tests if the given arguments matches according to tuProlog matching rules
     * for LogicTuples
     * <p>
     * No unification (a la tuProlog) is performed
     *
     * @param template the JTupleTemplate to match
     * @param tuple    the JTuple to match
     * @return wether the given JTupleTemplate and the given JTuple match
     * (according to tuProlog matching rules for LogicTuples)
     */
    public static boolean match(final JTupleTemplate template,
                                final JTuple tuple) {
        return LogicMatchingEngine.match(JTuplesEngine.toLogicTuple(template),
                JTuplesEngine.toLogicTuple(tuple));
    }

    /**
     * Tries to perform unification (a la tuProlog)
     *
     * @param template the JTupleTemplate to match
     * @param tuple    the JTuple to match
     * @return wether the given JTupleTemplate and the given JTuple match
     * (according to tuProlog matching rules for LogicTuples)
     */
    public static boolean propagate(final JTupleTemplate template,
                                    final JTuple tuple) {
        return LogicMatchingEngine.propagate(
                JTuplesEngine.toLogicTuple(template),
                JTuplesEngine.toLogicTuple(tuple));
    }

    /**
     * @param tuple the tuProlog LogicTuple to convert into a JTuple
     * @return the obtained JTuple
     * @throws InvalidTupleException if the given tuProlog LogicTuple is more expressive w.r.t.
     *                               JTuple language, hence, not convertible
     */
    public static JTuple toJavaTuple(final LogicTuple tuple)
            throws InvalidTupleException {
        TupleArgument ta;
        JVal[] vals = null;
        JTuple jt;
        try {
            if ("javat".equals(tuple.getName())) {
                final int a = tuple.getArity();
                vals = new JValDefault[a];
                for (int i = 0; i < a; i++) {
                    ta = tuple.getArg(i);
                    if (ta.getArity() == 1) {
                        switch (ta.getName()) {
                            case "double":
                                vals[i] = new JValDefault(ta.getArg(0).doubleValue());
                                break;
                            case "float":
                                vals[i] = new JValDefault(ta.getArg(0).floatValue());
                                break;
                            case "int":
                                vals[i] = new JValDefault(ta.getArg(0).intValue());
                                break;
                            case "literal":
                                vals[i] = new JValDefault(ta.getArg(0).toString());
                                break;
                            case "long":
                                vals[i] = new JValDefault(ta.getArg(0).longValue());
                                break;
                        }
                    } else {
                        throw new InvalidTupleException();
                    }
                }
            } else {
                throw new InvalidTupleException(
                        "Error occurred while converting '" + tuple.toString()
                                + "' into JTuple");
            }
        } catch (final InvalidJValException e) {
            // cannot happen
            Logger.getLogger("JTuplesEngine").log(Level.FINEST,
                    "Error: InvalidJValException");
        }
        jt = new JTupleDefault(vals[0]);
        for (int i = 1; i < vals.length; i++) {
            jt.addArg(vals[i]);
        }
        return jt;
    }

    /**
     * @param template the tuProlog LogicTuple template to convert into a
     *                 JTupleTemplate
     * @return the obtained JTupleTemplate
     * @throws InvalidTupleException if the given tuProlog LogicTuple template is more expressive
     *                               w.r.t. JTupleTemplate language, hence, not convertible
     */
    public static JTupleTemplate toJavaTupleTemplate(final LogicTuple template)
            throws InvalidTupleException {
        TupleArgument ta;
        TupleArgument ta2;
        List<JArg> args = null;
        JTupleTemplate jtt;
        try {
            if ("javat".equals(template.getName())) {
                final int a = template.getArity();
                args = new ArrayList<>(a);
                for (int i = 0; i < a; i++) {
                    ta = template.getArg(i);
                    switch (ta.getArity()) {
                        case 0:
                            if (ta.isVar()) {
                                args.add(new JVarDefault(JArgType.ANY));
                            } else {
                                throw new InvalidTupleException();
                            }
                            break;
                        case 1:
                            ta2 = ta.getArg(0);
                            if (ta2.isVar()) {
                                switch (ta.getName()) {
                                    case "double":
                                        args.add(new JVarDefault(JArgType.DOUBLE));
                                        break;
                                    case "float":
                                        args.add(new JVarDefault(JArgType.FLOAT));
                                        break;
                                    case "int":
                                        args.add(new JVarDefault(JArgType.INT));
                                        break;
                                    case "literal":
                                        args.add(new JVarDefault(JArgType.LITERAL));
                                        break;
                                    case "long":
                                        args.add(new JVarDefault(JArgType.LONG));
                                        break;
                                }
                            } else {
                                switch (ta.getName()) {
                                    case "double":
                                        args.add(new JValDefault(ta2.doubleValue()));
                                        break;
                                    case "float":
                                        args.add(new JValDefault(ta2.floatValue()));
                                        break;
                                    case "int":
                                        args.add(new JValDefault(ta2.intValue()));
                                        break;
                                    case "literal":
                                        args.add(new JValDefault(ta2.toString()));
                                        break;
                                    case "long":
                                        args.add(new JValDefault(ta2.longValue()));
                                        break;
                                }
                            }
                            break;
                        default:
                            throw new InvalidTupleException();
                    }
                }
            } else {
                throw new InvalidTupleException(
                        "Error occurred while converting '"
                                + template.toString() + "' into JTuple");
            }
        } catch (final InvalidJValException e) {
            // cannot happen
            Logger.getLogger("JTuplesEngine").log(Level.FINEST,
                    "Error: InvalidJValException");
        } catch (final InvalidJVarException e) {
            // cannot happen
            Logger.getLogger("JTuplesEngine").log(Level.FINEST,
                    "Error: InvalidJVarException");
        }
        jtt = new JTupleTemplateDefault(args.get(0));
        for (int i = 1; i < args.size(); i++) {
            jtt.addArg(args.get(i));
        }
        return jtt;
    }

    /**
     * @param tuple the JTuple to convert into a tuProlog LogicTuple
     * @return the obtained tuProlog LogicTuple
     */
    public static LogicTuple toLogicTuple(final JTuple tuple) {
        final JTupleDefault jt = (JTupleDefault) tuple;
        final TupleArgument[] tas = new TupleArgument[jt.getNArgs()];
        int i = 0;
        for (final JVal val : jt) {
            if (val.isDouble()) {
                tas[i] = TupleArguments.newValueArgument("double", TupleArguments.newValueArgument(val.toDouble()));
            } else if (val.isFloat()) {
                tas[i] = TupleArguments.newValueArgument("float", TupleArguments.newValueArgument(val.toFloat()));
            } else if (val.isInt()) {
                tas[i] = TupleArguments.newValueArgument("int", TupleArguments.newValueArgument(val.toInt()));
            } else if (val.isLiteral()) {
                tas[i] = TupleArguments.newValueArgument("literal", TupleArguments.newValueArgument(val.toLiteral()));
            } else if (val.isLong()) {
                tas[i] = TupleArguments.newValueArgument("long", TupleArguments.newValueArgument(val.toLong()));
            }
            i++;
        }
        return LogicTuples.newInstance("javat", tas);
    }

    /**
     * @param template the JTupleTemplate to convert into a tuProlog LogicTuple
     * @return the obtained tuProlog LogicTuple
     */
    public static LogicTuple toLogicTuple(final JTupleTemplate template) {
        final JTupleTemplateDefault jt = (JTupleTemplateDefault) template;
        final TupleArgument[] tas = new TupleArgument[jt.getNArgs()];
        int i = 0;
        for (final JArg arg : jt) {
            if (arg.isVal()) {
                final JVal val = (JVal) arg;
                if (val.isDouble()) {
                    tas[i] = TupleArguments.newValueArgument("double", TupleArguments.newValueArgument(val.toDouble()));
                } else if (val.isFloat()) {
                    tas[i] = TupleArguments.newValueArgument("float", TupleArguments.newValueArgument(val.toFloat()));
                } else if (val.isInt()) {
                    tas[i] = TupleArguments.newValueArgument("int", TupleArguments.newValueArgument(val.toInt()));
                } else if (val.isLiteral()) {
                    tas[i] = TupleArguments.newValueArgument("literal", TupleArguments.newValueArgument(val.toLiteral()));
                } else if (val.isLong()) {
                    tas[i] = TupleArguments.newValueArgument("long", TupleArguments.newValueArgument(val.toLong()));
                } else {
                    Logger.getLogger("JTuplesEngine").log(Level.FINEST,
                            "Error: Invalid JVal type");
                }
            } else if (arg.isVar()) {
                final JVar var = (JVar) arg;
                switch (var.getType()) {
                    case ANY:
                        tas[i] = TupleArguments.newVarArgument();
                        break;
                    case DOUBLE:
                        tas[i] = TupleArguments.newValueArgument("double", TupleArguments.newVarArgument());
                        break;
                    case FLOAT:
                        tas[i] = TupleArguments.newValueArgument("float", TupleArguments.newVarArgument());
                        break;
                    case INT:
                        tas[i] = TupleArguments.newValueArgument("int", TupleArguments.newVarArgument());
                        break;
                    case LITERAL:
                        tas[i] = TupleArguments.newValueArgument("literal", TupleArguments.newVarArgument());
                        break;
                    case LONG:
                        tas[i] = TupleArguments.newValueArgument("long", TupleArguments.newVarArgument());
                        break;
                    default:
                        // cannot happen
                        Logger.getLogger("JTuplesEngine").log(Level.FINEST,
                                "Error: Invalid JVar type");
                        break;
                }
            }
            i++;
        }
        return LogicTuples.newInstance("javat", tas);
    }

    private JTuplesEngine() {
        // to prevent instantiation
    }
}
