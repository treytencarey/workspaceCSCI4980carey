/*
 * @(#) MyNode.java
 *
 */
package graph.model;

import java.util.ArrayList;
import java.util.List;

public class GNode {
   private final String id;
   private final String name;
   private final String parent;
   private List<GNode>  connections;
   private GNodeType    type;
   private String declType;

   public GNode(String id, String name, String parent, String declType) {
      this.id = id;
      this.name = name;
      this.parent = parent;
      this.connections = new ArrayList<GNode>();
      this.type = GNodeType.InValid;
      this.declType = declType;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getParent() {
      return this.parent;
   }

   public List<GNode> getConnectedTo() {
      return connections;
   }

   public void setNodeType(GNodeType type) {
      this.type = type;
   }

   public GNodeType getNodeType() {
      return this.type;
   }

   public boolean eq(GNode n) {
      if (n == null)
         return false;
      return this.parent.equals(n.getParent()) && this.name.equals(n.getName());
   }
   
   public String getDeclType() {
	   return this.declType;
   }
}
