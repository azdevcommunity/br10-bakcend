package fib.br10.core.utility;

import com.p6spy.engine.spy.appender.FormattedLogger;
import lombok.extern.log4j.Log4j2;
import com.p6spy.engine.logging.Category;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
@Log4j2
public class CustomSQLFormat extends FormattedLogger {

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        if (sql != null && !sql.trim().isEmpty()) {
            log.info("\nTime elapsed: {} ms\nSQL query:\n{}", elapsed, formatSql(sql));
        }

    }

    private String formatSql(String sql) {
        try {
            Statement stmt = CCJSqlParserUtil.parse(sql);
            StatementDeParser deParser = new StatementDeParser(new StringBuilder());
            stmt.accept(deParser);
            return deParser.getBuffer().toString()
                    .replaceAll("SELECT ", "SELECT\n\t")
                    .replaceAll(", ", ",\n\t")
                    .replaceAll(" FROM ", "\nFROM\n\t")
                    .replaceAll(" WHERE ", "\nWHERE\n\t");
        } catch (Exception e) {
            return sql;
        }
    }

    @Override
    public void logException(Exception e) {
    }

    @Override
    public void logText(String text) {
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        return true;
    }
}