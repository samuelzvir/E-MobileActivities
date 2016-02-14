package br.com.samuelzvir.meuabc;
import java.util.ArrayList;
import java.util.List;

import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;


public class EMATestCase extends AndroidTestCase {

    protected void assertM2M(String table1, String table2, long id1, long id2) {
        assertTrue(isIntermediateDataCorrect(table1, table2, id1, id2));
    }

    protected void assertM2MFalse(String table1, String table2, long id1, long id2) {
        assertFalse(isIntermediateDataCorrect(table1, table2, id1, id2));
    }

    /**
     *
     * @param table1
     *            Table without foreign key.
     * @param table2
     *            Table with foreign key.
     * @param table1Id
     *            id of table1.
     * @param table2Id
     *            id of table2.
     * @return success or failed.
     */
    protected boolean isFKInsertCorrect(String table1, String table2, long table1Id, long table2Id) {
        SQLiteDatabase db = Connector.getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(table2, null, "id = ?", new String[] { String.valueOf(table2Id) },
                    null, null, null);
            cursor.moveToFirst();
            long fkId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseUtility.changeCase(table1
                    + "_id")));
            return fkId == table1Id;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cursor.close();
        }
    }

    protected boolean isIntermediateDataCorrect(String table1, String table2, long table1Id,
                                                long table2Id) {
        SQLiteDatabase db = Connector.getDatabase();
        Cursor cursor = null;
        try {
            String where = table1 + "_id = ? and " + table2 + "_id = ?";
            cursor = db.query(DBUtility.getIntermediateTableName(table1, table2), null, where,
                    new String[] { String.valueOf(table1Id), String.valueOf(table2Id) }, null,
                    null, null);
            return cursor.getCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cursor.close();
        }
    }

    protected long getForeignKeyValue(String tableWithFK, String tableWithoutFK, long id) {
        Cursor cursor = Connector.getDatabase().query(tableWithFK, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        long foreignKeyId = 0;
        if (cursor.moveToFirst()) {
            foreignKeyId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseUtility
                    .changeCase(tableWithoutFK + "_id")));
        }
        cursor.close();
        return foreignKeyId;
    }

    protected boolean isDataExists(String table, long id) {
        SQLiteDatabase db = Connector.getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(table, null, "id = ?", new String[] { String.valueOf(id) }, null,
                    null, null);
            return cursor.getCount() == 1 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    protected String getTableName(Object object) {
        return DBUtility.getTableNameByClassName(object.getClass().getName());
    }

    protected String getTableName(Class<?> c) {
        return DBUtility.getTableNameByClassName(c.getName());
    }

    protected int getRowsCount(String tableName) {
        int count = 0;
        Cursor c = Connector.getDatabase().query(tableName, null, null, null, null, null, null);
        count = c.getCount();
        c.close();
        return count;
    }



    private String getWhere(int[] ids) {
        StringBuilder where = new StringBuilder();
        boolean needOr = false;
        for (int id : ids) {
            if (needOr) {
                where.append(" or ");
            }
            where.append("id = ").append(id);
            needOr = true;
        }
        return where.toString();
    }

}