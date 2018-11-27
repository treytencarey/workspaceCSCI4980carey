/*
 * @(#) GClassNode.java
 *
 */
package graph.model;

public class GClassNode extends GNode {
   private String pkgName;
   private String type;

   public GClassNode(String id, String name, String parent, String type) {
      super(id, name, parent, type);
   }

   public String getPkgName() {
      return this.pkgName;
   }

   public void setPkgName(String pkgName) {
      this.pkgName = pkgName;
   }
   
   public String getType() {
	   return this.type;
   }
}
