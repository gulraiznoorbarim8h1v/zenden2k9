package com.zenden2k.VfFrameworkIdeaPlugin.reference;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import com.zenden2k.VfFrameworkIdeaPlugin.dom.object.Object;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class XmlObjectReference implements /*PsiPolyVariantReference*/ PsiReference {
    protected final PsiElement element;
    protected final TextRange textRange;
    protected final Project project;
    protected final String objectName;


    public XmlObjectReference(String objectName, PsiElement element, TextRange textRange, Project project) {
        this.element = element;
        this.textRange = textRange;
        this.project = project;
        this.objectName = objectName;
    }

    @Override
    public String toString() {
        return getCanonicalText();
    }

    @Override @NotNull
    public PsiElement getElement() {
        return this.element;
    }

    @Override @NotNull
    public TextRange getRangeInElement() {
        return textRange;
    }

    @Override public PsiElement handleElementRename(@NotNull String newElementName)
            throws IncorrectOperationException {
        // TODO: Implement this method
        throw new IncorrectOperationException();
    }

    @Override public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        // TODO: Implement this method
        throw new IncorrectOperationException();
    }

    @Override public boolean isReferenceTo(@NotNull PsiElement element) {
        return resolve() == element;
    }

    @Override @NotNull
    public Object[] getVariants() {
        // TODO: Implement this method
        return new Object[0];
    }

    @Override public boolean isSoft() {
        return false;
    }

    @Override
    @Nullable
    public PsiElement resolve() {
        if (objectName != null) {
            VirtualFile[] vFiles = ProjectRootManager.getInstance(this.project).getContentRoots();
            if (vFiles.length != 0 ) {
                int delimPos = objectName.indexOf(":");
                String xmlFileName;
                String directoryName;
                if (delimPos != -1) {
                    directoryName = objectName.substring(0, delimPos);
                    xmlFileName = objectName.substring(delimPos + 1);
                } else {
                    directoryName = objectName;
                    xmlFileName = objectName;
                }

                VirtualFile vf = vFiles[0].findFileByRelativePath("system/application/vf_controllers/" + directoryName + "/" + xmlFileName + ".xml");
                if (vf != null) {

                    PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
                    if (psiFile instanceof XmlFile) {

                        XmlFile xmlFile = (XmlFile)psiFile;
                        /*DomManager manager = DomManager.getDomManager(project);
                        DomFileElement<Object> el = manager.getFileElement(xmlFile, Object.class);
                        if(el!=null) {
                            Object root = el.getRootElement();
                            return root.getXmlElement();
                            /*Fields fd = root.getFields();
                            List<Field> fds = fd.getFields();*/
                            /*DomTarget domTarget = DomTarget.getTarget(root);

                            if (domTarget != null) {
                                return PomService.convertToPsi(domTarget);
                            }*
                        }*/

                        //Fields f = el.getFields();
                        XmlTag tag = xmlFile.getRootTag();
                        if (tag != null) {
                            return tag;
                        }
                        return xmlFile;
                    }
                }
            }

        }
        return null;

    }

    @Override
    @NotNull
    public String getCanonicalText() {
        return objectName;
    }

}
