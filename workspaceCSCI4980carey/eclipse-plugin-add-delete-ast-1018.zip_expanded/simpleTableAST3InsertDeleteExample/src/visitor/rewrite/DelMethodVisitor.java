package visitor.rewrite;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import model.ProgramElement;
import util.UtilMsg;

public class DelMethodVisitor extends ASTVisitor {
   private ProgramElement progElemToBeRemoved;
   private MethodDeclaration methodToBeRemoved;
   private ASTRewrite rewrite;
   private boolean isEmpty = false;
   boolean isDeleted = false;

   public DelMethodVisitor(ProgramElement curProgElem) {
      this.progElemToBeRemoved = curProgElem;
      this.isEmpty = false;
      this.isDeleted = false;
   }

   public void setASTRewrite(ASTRewrite rewrite) {
      this.rewrite = rewrite;
   }

   @Override
   public void endVisit(TypeDeclaration typeDecl) {
      // TODO: class exercise
      ListRewrite lrw = rewrite.getListRewrite(typeDecl, //
            TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
      lrw.remove(methodToBeRemoved, null);
      isDeleted = true;
   }

   public boolean visit(MethodDeclaration node) {
      String name = node.getName().getFullyQualifiedName();
      if (name.equals(progElemToBeRemoved.getMethodName())) {

         List<?> statements = node.getBody().statements();
         // TODO: class exercise
         this.methodToBeRemoved = node;
         return false;
      }
      return true;
   }

   public boolean isDeleted() {
      return isDeleted;
   }

   public void setDeleted(boolean isDeleted) {
      this.isDeleted = isDeleted;
   }
}
