package user.theovercaste.overdecompiler.goals;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.ArgumentHandler;
import user.theovercaste.overdecompiler.classdataloaders.FileClassDataLoader;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.filters.*;
import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parsers.JavaParser;
import user.theovercaste.overdecompiler.printerdata.IndentationStrategySpaces;
import user.theovercaste.overdecompiler.printerdata.variablenamers.SimpleVariableNamer;
import user.theovercaste.overdecompiler.printers.*;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

import com.google.common.collect.ObjectArrays;

public class GoalDecompile extends AbstractGoalByteEditor {
    private final Logger logger = LoggerFactory.getLogger(GoalDecompile.class);

    protected List<Filter> filters = new ArrayList<>();

    protected AbstractPrinterFactory printerFactory;
    protected boolean recursive;
    protected boolean threaded;
    protected boolean matchLineNumbers; // TODO

    @Override
    public void execute(ArgumentHandler h) throws ArgumentParsingException {
        if (isHelpRequest(h)) {
            sendUsageMessage(System.out);
            return;
        }
        printerFactory = new JavaPrinter.Factory(new SimpleVariableNamer(), new IndentationStrategySpaces(2)); // TODO
        // printerFactory = new DebugPrinter.Factory();
        // printerFactory = h.getClassArgument("printer", "user.theovercaste.overdecompiler.printers", printerFactory, AbstractPrinterFactory.class);
        recursive = h.checkFlagExists('r') || h.checkFlagExists("recursive");
        threaded = h.checkFlagExists('t') || h.checkFlagExists("threaded");
        matchLineNumbers = h.checkFlagExists('m') || h.checkFlagExists("match-line-numbers"); // TODO
        filters.add(new FilterRemoveImplicitConstructor());
        filters.add(new FilterRemoveImplicitExtends());
        filters.add(new FilterRemoveImplicitReturn());
        processData(h.getArgument(h.getArgumentsSize() - 1), recursive);
    }

    @Override
    protected void processFiles(Collection<File> files) {
        int size = files.size();
        if (size == 0) {
            return;
        }
        if (threaded) {
            int threadCount = Runtime.getRuntime().availableProcessors();
            ExecutorService threads = Executors.newFixedThreadPool(threadCount > size ? size : threadCount);
            ArrayList<File> sortedFiles = new ArrayList<File>(files.size());
            sortedFiles.addAll(files);
            Collections.sort(sortedFiles, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return (int) (f1.length() - f2.length());
                }
            });
            DecompilerTask[] tasks = new DecompilerTask[threadCount];
            for (int i = 0; i < tasks.length; i++) {
                tasks[i] = new DecompilerTask();
            }
            for (int i = 0; i < sortedFiles.size(); i++) {
                tasks[i % tasks.length].addFile(sortedFiles.get(i));
            }
            ArrayList<Future<?>> taskFutures = new ArrayList<Future<?>>(tasks.length);
            for (DecompilerTask t : tasks) {
                taskFutures.add(threads.submit(t));
            }
            for (Future<?> future : taskFutures) {
                try {
                    future.get(); // Block until done
                } catch (InterruptedException e) {
                    logger.warn("A concurrent decompilation task was interrupted.", e);
                } catch (ExecutionException e) {
                    logger.warn("A concurrent decompilation task threw an exception.", e);
                }
            }
        } else {
            DecompilerTask decompiler = new DecompilerTask(); // Do it all in one task
            for (File f : files) {
                decompiler.addFile(f);
            }
            decompiler.run(); // No point starting a new thread, execute blocking
        }
    }

    @Override
    protected String[] getUsage( ) {
        return ObjectArrays.concat(ArgumentHandler.getDefaultUsage(), new String[] {
                "  --parser (class)\t\tSpecify a parser to be used.",
                "  --printer (class)\t\tSpecify the printer to be used.",
                "  --threaded  -t\t\tWhether classes should be decompiled concurrently.",
                "  --recursive  -r\t\tSpecify that recursive checking should be done.",
                // TODO "  --match-line-numbers -m\tTry to match each decompiled line with it's original location in the source."
        }, String.class);
    }

    private class DecompilerTask implements Runnable {
        private final ArrayList<File> files = new ArrayList<File>();
        private final Printer printer = printerFactory.createPrinter();

        @Override
        public void run( ) {
            for (File f : files) {
                logger.info(" - Reading binary data...");
                try (FileClassDataLoader loader = new FileClassDataLoader(f)) {
                    ClassData c = loader.getClassData();
                    logger.info(" - Parsing...");
                    ParsedClass parsed = (new JavaParser(c).parseClass());
                    logger.info(" - Filtering...");
                    for (Filter filter : filters) {
                        filter.apply(parsed);
                    }
                    logger.info(" - Writing...");
                    printer.print(parsed, System.out);
                } catch (InvalidClassException ex) {
                    logger.error("Couldn't load the class data from file {}. Is it a valid java class file?", f.getPath(), ex);
                } catch (ClassParsingException ex) {
                    logger.error("Couldn't parse class data from file {}. Is it in a class version supported?", f.getPath(), ex);
                } catch (FileNotFoundException ex) {
                    logger.error("Failed to open a file for decompilation: {}.", ex);
                } catch (IOException ex) {
                    logger.error("Couldn't print the class to the output stream specified.", ex);
                }
            }
        }

        public void addFile(File f) {
            files.add(f);
        }
    }

    public static class Factory implements AbstractGoalFactory {
        private static final Factory instance = new Factory();

        private Factory( ) {
            // Do nothing
        }

        public static Factory getInstance( ) {
            return instance;
        }

        @Override
        public AbstractGoal createAction( ) {
            return new GoalDecompile();
        }
    }
}
