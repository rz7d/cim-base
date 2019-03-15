package milktea.cim.framework.interactive;

import milktea.cim.framework.permission.Permission;

public interface Account {

  boolean hasPermission(Permission permission);

}
