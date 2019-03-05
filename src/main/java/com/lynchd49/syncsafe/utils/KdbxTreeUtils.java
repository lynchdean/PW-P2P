package com.lynchd49.syncsafe.utils;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Group;

public class KdbxTreeUtils {
    public static TreeView<String> getTreeView(Database db) {
        Group rootGroup = db.getRootGroup();
        TreeItem<String> rootItem = getChildrenLeavesRecursive(rootGroup);
        rootItem.setExpanded(true);
        return new TreeView<>(rootItem);
    }

    private static TreeItem<String> getChildrenLeavesRecursive(Group g){
        TreeItem<String> groupLeaf = new TreeItem<>(g.getName());
        if (g.getGroupsCount() > 0) {
            for (Object childObj : g.getGroups()) {
                Group child = (Group) childObj;
                groupLeaf.getChildren().add(getChildrenLeavesRecursive(child));
            }
        }
        return groupLeaf;
    }
}
