package com.lynchd49.syncsafe.utils;

import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Group;

import java.util.ArrayList;
import java.util.List;

public class KdbxTreeUtils {

    public static TreeItem<String> getTreeRoot(Database db) {
        Group rootGroup = db.getRootGroup();
        TreeItem<String> rootItem = getChildrenLeavesRecursive(rootGroup);
        rootItem.setExpanded(true);
        return rootItem;
    }

    private static TreeItem<String> getChildrenLeavesRecursive(Group g){
        FontIcon folderIcon = new FontIcon("fa-folder");
        folderIcon.setIconColor(Color.CORNFLOWERBLUE);
        TreeItem<String> groupLeaf = new TreeItem<>(g.getName(), folderIcon);
        if (g.getGroupsCount() > 0) {
            for (Object childObj : g.getGroups()) {
                Group child = (Group) childObj;
                groupLeaf.getChildren().add(getChildrenLeavesRecursive(child));
            }
        }
        return groupLeaf;
    }

    public static List<String> getTreeItemPath(TreeItem treeItem) {
        List<String> path = new ArrayList<>();
        if (treeItem != null) {
            path.add(0, treeItem.getValue().toString());
            treeItem = treeItem.getParent();
            while (treeItem != null) {
                path.add(0, treeItem.getValue().toString());
                treeItem = treeItem.getParent();
            }
        }
        return path;
    }

    public static Group getGroupFromPath(Database db, List<String> path) {
        Group group = db.getRootGroup();
        if (path.size() == 0) {
            return group;
        }
        path.remove(0);  // Remove Root from path
        while (!path.isEmpty()) {
            List children = group.getGroups();
            for (Object childObj : children) {
                Group child = (Group) childObj;
                if (child.getName().equals(path.get(0))) {
                    group = child;
                    path.remove(0);
                    break;
                }
            }
        }
        return group;
    }


}
