package org.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.configuration.imports.*;
import com.github.javaparser.printer.configuration.PrettyPrinterConfiguration;
import com.github.javaparser.printer.YamlPrinter;
import com.github.javaparser.printer.DotPrinter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * The JavaCodeAnalyzer Program implements an application that
 * analyzes a given Java source file and generates a parse tree
 * and statistics on the contents of the code.
 * 
 * @author Victor C. Chabunda
 * @version 0.0.1
 * @since 2024-04-22
 */

public class JavaCodeAnalyzer {
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage: java JavaCodeAnalyzer <source_file_path> <output_directory>");
            return;
        }

        String srcFile = args[0];
        String outputDir = args[1];

        try {
            CompilationUnit cu = StaticJavaParser.parse(new File(srcFile));

            File outputDirectory = new File(outputDir);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            new PrettyPrintVisitor().visit(cu, outputDir);

            new DotPrintVisitor().visit(cu, outputDir);

            new YamlPrintVisitor().visit(cu, outputDir);

            String statsFileName = outputDir + File.separator + "stats.txt";
            try (FileWriter writer = new FileWriter(statsFileName)) {
                writer.write("Stats:\n");
                cu.accept(new StatisticsVisitor(writer), null);
            }

            System.out.println("Parse tree and statistics saved to: " + outputDir);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + srcFile);
            e.printStackTrace();
        }
    }

    private static class PrettyPrintVisitor extends VoidVisitorAdapter<String> {
        @Override
        public void visit(CompilationUnit cu, String outputDir) {
            String fileName = outputDir + File.separator + "code.txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                PrettyPrinterConfiguration configuration = new PrettyPrinterConfiguration();
                configuration.setIndentSize(2);
                configuration.setEndOfLineCharacter("\n");
                PrettyPrinter prettyPrinter = new PrettyPrinter(configuration);
                writer.write(prettyPrinter.print(cu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class DotPrintVisitor extends VoidVisitorAdapter<String> {
        @Override
        public void visit(CompilationUnit cu, String outputDir) {
            String fileName = outputDir + File.separator + "ast.dot";
            try (FileWriter writer = new FileWriter(fileName)) {
                DotPrinter dotPrinter = new DotPrinter(true);
                writer.write(dotPrinter.output(cu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class YamlPrintVisitor extends VoidVisitorAdapter<String> {
        @Override
        public void visit(CompilationUnit cu, String outputDir) {
            String fileName = outputDir + File.separator + "ast.yaml";
            try (FileWriter writer = new FileWriter(fileName)) {
                YamlPrinter yamlPrinter = new YamlPrinter(true);
                writer.write(yamlPrinter.output(cu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class StatisticsVisitor extends VoidVisitorAdapter<Void> {

        private final FileWriter writer;

        public StatisticsVisitor(FileWriter writer) {
            this.writer = writer;
        }

        @Override
        public void visit(CompilationUnit cu, Void arg) {
            super.visit(cu, arg);
            int numClasses = cu.findAll(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class).size();
            int numMethods = cu.findAll(com.github.javaparser.ast.body.MethodDeclaration.class).size();
            int numStatements = cu.findAll(com.github.javaparser.ast.stmt.Statement.class).size();
            int numImports = cu.findAll(com.github.javaparser.ast.ImportDeclaration.class).size();
            int numFields = cu.findAll(com.github.javaparser.ast.body.FieldDeclaration.class).size();
            int numConstructors = cu.findAll(com.github.javaparser.ast.body.ConstructorDeclaration.class).size();
            int numInterfaces = (int) cu.findAll(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                    .stream()
                    .filter(decl -> decl.isInterface()).count();
            int numEnums = cu.findAll(com.github.javaparser.ast.body.EnumDeclaration.class).size();
            int numAnnotations = cu.findAll(com.github.javaparser.ast.body.AnnotationDeclaration.class).size();
            int numComments = cu.findAll(com.github.javaparser.ast.comments.Comment.class).size();
            int numIfStatements = cu.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size();
            int numForLoops = cu.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size();
            int numWhileLoops = cu.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size();
            int numTryCatchBlocks = cu.findAll(com.github.javaparser.ast.stmt.TryStmt.class).size();
            int numSwitchStatements = cu.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).size();
            int numLambdaExpressions = cu.findAll(com.github.javaparser.ast.expr.LambdaExpr.class).size();
            int numMethodReferences = cu.findAll(com.github.javaparser.ast.expr.MethodReferenceExpr.class).size();
            int numAssertStatements = cu.findAll(com.github.javaparser.ast.stmt.AssertStmt.class).size();
            int numSynchronizedBlocks = cu.findAll(com.github.javaparser.ast.stmt.SynchronizedStmt.class).size();
            int numThrowStatements = cu.findAll(com.github.javaparser.ast.stmt.ThrowStmt.class).size();
            int numGenerics = cu.findAll(com.github.javaparser.ast.type.TypeParameter.class).size();
            int numAnnotationUses = cu.findAll(com.github.javaparser.ast.expr.AnnotationExpr.class).size();
            int numInnerClasses = (int) cu.findAll(com.github.javaparser.ast.body.TypeDeclaration.class).stream()
                    .filter(decl -> decl.getParentNode().isPresent()
                            && decl.getParentNode().get() instanceof com.github.javaparser.ast.body.TypeDeclaration)
                    .count();

            try {

                String format = "| %-30s | %-5d |\n";
                writer.write("+--------------------------------+-------+\n");
                writer.write("| Statistic                      | Count |\n");
                writer.write("+--------------------------------+-------+\n");
                writer.write(String.format(format, "Classes", numClasses));
                writer.write(String.format(format, "Methods", numMethods));
                writer.write(String.format(format, "Statements", numStatements));
                writer.write(String.format(format, "Imports", numImports));
                writer.write(String.format(format, "Fields", numFields));
                writer.write(String.format(format, "Constructors", numConstructors));
                writer.write(String.format(format, "Interfaces", numInterfaces));
                writer.write(String.format(format, "Enums", numEnums));
                writer.write(String.format(format, "Annotations", numAnnotations));
                writer.write(String.format(format, "Comments", numComments));
                writer.write(String.format(format, "If Statements", numIfStatements));
                writer.write(String.format(format, "For Loops", numForLoops));
                writer.write(String.format(format, "While Loops", numWhileLoops));
                writer.write(String.format(format, "Try-Catch Blocks", numTryCatchBlocks));
                writer.write(String.format(format, "Switch Statements", numSwitchStatements));
                writer.write(String.format(format, "Lambda Expressions", numLambdaExpressions));
                writer.write(String.format(format, "Method References", numMethodReferences));
                writer.write(String.format(format, "Assert Statements", numAssertStatements));
                writer.write(String.format(format, "Synchronized Blocks", numSynchronizedBlocks));
                writer.write(String.format(format, "Throw Statements", numThrowStatements));
                writer.write(String.format(format, "Generics", numGenerics));
                writer.write(String.format(format, "Annotation Uses", numAnnotationUses));
                writer.write(String.format(format, "Inner Classes", numInnerClasses));
                writer.write("+--------------------------------+-------+\n");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

        }
    }
}