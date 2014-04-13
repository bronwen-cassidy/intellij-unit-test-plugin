/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 03-Mar-2006
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package org.intellij.plugins.junit.refactoring;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.MoveClassesOrPackagesRefactoring;
import com.intellij.refactoring.MoveDestination;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.JavaRefactoringFactory;
import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.adaptor.RefactoringAdaptor;
import org.intellij.plugins.util.PluginPsiUtil;

/**
 * Refactoring class to handle demetra specific refactoring
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Jul-2005 08:02:26
 */
public class SelenaRefactorer implements RefactoringAdaptor {

    private Project project;
    private PluginPsiUtil pluginPsiUtil;

    public SelenaRefactorer(Project project, PluginPsiUtil pluginPsiUtil) {
        this.project = project;
        this.pluginPsiUtil = pluginPsiUtil;
    }

    public void rename(PsiNamedElement[] elementsToBeRenamed, String[] newNames) {
        RenameRefactoring rename = createRenameRefactoring(elementsToBeRenamed[0], newNames[0]);
        if (rename == null) return;
        for (int i = 1; i < elementsToBeRenamed.length; i++) {
            PsiNamedElement namedElement = elementsToBeRenamed[i];
            if (!isContainingFileWritable(namedElement)) return;
            rename.addElement(namedElement, newNames[i]);
        }
        rename.run();
    }

    public void moveClasses(TestClassDef[] testClassDefs) {
        for (int i = 0; i < testClassDefs.length; i++) {
            TestClassDef testClassDef = testClassDefs[i];
            PsiClass classToMove = testClassDef.getPsiClass();
            PsiDirectory newParent = testClassDef.getRoot();
            moveClass(classToMove, newParent);
        }
    }

    protected boolean isContainingFileWritable(PsiElement element) {
        return pluginPsiUtil.isFileWritable(element);
    }

    private void moveClass(PsiClass classToBeMoved, PsiDirectory newParent) {
        debug("moving " + classToBeMoved.getQualifiedName() + " to " + newParent.getVirtualFile().getPath());

        if (!isContainingFileWritable(classToBeMoved)) return;

        //RefactoringFactory refactoringFactory = RefactoringFactory.getInstance(project);
        JavaRefactoringFactory refactoringFactory = JavaRefactoringFactory.getInstance(project);
        final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(newParent);
        String qualifiedName = aPackage != null ? aPackage.getQualifiedName() : "";

        MoveDestination destination = refactoringFactory.createSourceFolderPreservingMoveDestination(qualifiedName);
        //MoveDestination destination = refactoringFactory.createSourceRootMoveDestination(qualifiedName, newParent.getVirtualFile());
        MoveClassesOrPackagesRefactoring mover = refactoringFactory.createMoveClassesOrPackages(new PsiClass[]{classToBeMoved}, destination);
        mover.run();
    }

    private RenameRefactoring createRenameRefactoring(PsiNamedElement elementToBeRenamed, String newName) {
        if (!isContainingFileWritable(elementToBeRenamed)) return null;
        return RefactoringFactory.getInstance(project).createRename(elementToBeRenamed, newName);
    }

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    private static final Logger LOG = Logger.getInstance(SelenaRefactorer.class.getName());
}