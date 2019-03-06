package com.lynchd49.syncsafe.utils

import javafx.scene.Scene
import javafx.scene.control.TreeView
import javafx.scene.layout.HBox
import javafx.stage.Stage
import org.linguafranca.pwdb.Database
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

class KdbxTreeUtilsTest extends ApplicationSpec {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"
    final static String dbPath1 = dir + "test1.kdbx"
    final static String dbPw1 = "test1"

    private Database db

    @Override
    void init() throws Exception {
        FxToolkit.registerStage {
            new Stage()
        }
    }

    @Override
    void start(Stage stage) {
        db = KdbxOps.loadKdbx(dbPath1, dbPw1)
        TreeView treeView = KdbxTreeUtils.getTreeView(db)
        treeView.setId("treeView")

        HBox hbox = new HBox()
        hbox.getChildren().add(treeView)

        stage.setScene(new Scene(hbox, 100, 100))
        stage.show()
    }

    @Override
    void stop() throws Exception {
        FxToolkit.hideStage()
    }

    def "GetTreeView"() {
    }

    def "Should return root path given a TreeItem"() {
//        TreeView treeView = find(".treeView")
//        TreeItem treeItem = treeView.getTreeItem(5)
//
//        when:
//        String path = KdbxTreeUtils.getTreeItemPath(treeItem)
//
//        then:
//        path == "test"
    }
}
