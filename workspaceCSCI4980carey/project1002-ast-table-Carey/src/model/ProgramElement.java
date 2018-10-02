package model;

public class ProgramElement {
   private String pkgName;
   private String className;
   private String methodName;
   private boolean isReturnVoid;
   private Integer parameterSize;
   private Integer methodStartingPosition;

   public ProgramElement() {
   }

   public ProgramElement(String pkgName, String className, String methodName, boolean isRetVoid, int parmSize, int methodStartPos) {
      this.pkgName = pkgName;
      this.className = className;
      this.methodName = methodName;
      this.isReturnVoid = isRetVoid;
      this.parameterSize = parmSize;
      this.methodStartingPosition = methodStartPos;
   }

   public String getPkgName() {
      return pkgName;
   }

   public void setPkgName(String pkgName) {
      this.pkgName = pkgName;
   }

   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public String getMethodName() {
      return methodName;
   }

   public void setMethodName(String methodName) {
      this.methodName = methodName;
   }

   public boolean isReturnVoid() {
      return isReturnVoid;
   }

   public void setReturnVoid(boolean isReturnVoid) {
      this.isReturnVoid = isReturnVoid;
   }

   public Integer getParameterSize() {
      return parameterSize;
   }

   public void setParameterSize(Integer parameterSize) {
      this.parameterSize = parameterSize;
   }
   
   public Integer getMethodStartingPosition() {
	   return methodStartingPosition;
   }
   
   public void setMethodStartingPosition(Integer methodStartingPosition) {
	   this.methodStartingPosition = methodStartingPosition;
   }

   @Override
   public String toString() {
      return pkgName + "." + className + "." + methodName;
   }
}
