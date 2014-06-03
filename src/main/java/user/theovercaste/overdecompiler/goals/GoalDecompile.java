package user.theovercaste.overdecompiler.goals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import user.theovercaste.overdecompiler.ArgumentHandler;
import user.theovercaste.overdecompiler.classdataloaders.FileClassDataLoader;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parsers.AbstractParser;
import user.theovercaste.overdecompiler.parsers.JavaParser;
import user.theovercaste.overdecompiler.printers.AbstractPrinter;
import user.theovercaste.overdecompiler.printers.AbstractPrinterFactory;
import user.theovercaste.overdecompiler.printers.PrettyPrinter;

import com.google.common.collect.ObjectArrays;

public class GoalDecompile extends AbstractGoalByteEditor {
    protected AbstractParser.Factory parserFactory;
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
        parserFactory = h.getClassArgument("parser", "user.theovercaste.overdecompiler.parsers", JavaParser.Factory.getInstance(), AbstractParser.Factory.class);
        printerFactory = h.getClassArgument("printer", "user.theovercaste.overdecompiler.printers", PrettyPrinter.Factory.getInstance(), AbstractPrinterFactory.class);
        recursive = h.checkFlagExists('r') || h.checkFlagExists("recursive");
        threaded = h.checkFlagExists('t') || h.checkFlagExists("threaded");
        matchLineNumbers = h.checkFlagExists('m') || h.checkFlagExists("match-line-numbers"); // TODO
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
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
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
        private final AbstractPrinter printer = printerFactory.createPrinter();
        private final AbstractParser parser = parserFactory.createParser();

        @Override
        public void run( ) {
            for (File f : files) {
                System.out.println(" - Reading binary data...");
                try (FileClassDataLoader loader = new FileClassDataLoader(f)) {
                    ClassData c = loader.getClassData();
                    System.out.println(" - Parsing...");
                    ParsedClass parsed = parser.parseClass(c);
                    System.out.println(" - Writing...");
                    printer.print(parsed, System.out);
                } catch (InvalidConstantPoolPointerException | InvalidClassException e) {
                    System.err.println("Invalid class detected. (" + e.getMessage() + ")");
                } catch (IOException e) {
                    e.printStackTrace();
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
