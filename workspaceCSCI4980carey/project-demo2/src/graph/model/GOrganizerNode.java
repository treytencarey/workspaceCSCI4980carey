/*
 * @(#) GClassNode.java
 *
 */
package graph.model;

public class GOrganizerNode extends GNode {
   private String organizerName;

   public GOrganizerNode(String id, String name, String parent) {
      super(id, name, parent);
   }

   public String getOrganizerName() {
      return this.organizerName;
   }

   public void setOrganizerName(String organizerName) {
      this.organizerName = organizerName;
   }
}
