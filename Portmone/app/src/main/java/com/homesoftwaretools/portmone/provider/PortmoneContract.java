package com.homesoftwaretools.portmone.provider;
/*
 * Created by Wild on 02.05.2015.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class PortmoneContract {
    public static final String AUTHORITY = "com.homesoftwaretools.portmone.Provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public interface WebColumns extends BaseColumns {
        public static final String WEB_ID = "web_id";
        public static final String TOKEN = "token";
    }

    public static class Users implements WebColumns {
        public static final String USERS = "users";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, USERS);
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String NOTES = "notes";

        public static final String[] FIELDS = {NAME + " text", EMAIL + " text", TOKEN + " text",
                WEB_ID + " text", NOTES + " text"};

        private Users() {}
    }

    public interface BaseLib extends WebColumns {
        String NAME = "name";
        String[] FIELDS = {NAME + " text", TOKEN + " text", WEB_ID + " text"};
    }

    public static class CashTypes implements BaseLib {
        public static final String CASH_TYPES = "cash_types";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, CASH_TYPES);

        private CashTypes() {}
    }

    public static class IncomeTypes implements BaseLib {
        public static final String INCOME_TYPES = "income_types";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, INCOME_TYPES);

        private IncomeTypes() {}
    }

    public static class ExpenseTypes implements BaseLib {
        public static final String EXPENSE_TYPES = "expense_types";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, EXPENSE_TYPES);

        private ExpenseTypes() {}
    }

    public static class Tags implements BaseLib {
        public static final String TAGS = "tags";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, TAGS);

        private Tags() {}
    }

    public interface Journal extends WebColumns {
        String TIMESTAMP = "timestamp";
        String PLANNED = "planned";
        String SUM = "sum";
        String DESCRIPTION = "description";
        String DELETED = "deleted";
    }

    public static class Incomes implements Journal {
        public static final String INCOMES = "incomes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, INCOMES);
        public static final String CASH_TYPE_ID = "cash_type_id";
        public static final String INCOME_TYPE_ID = "income_type_id";
        public static final String[] FIELDS = {TIMESTAMP + " integer", PLANNED + " integer",
                CASH_TYPE_ID + " integer", INCOME_TYPE_ID + " integer", SUM + " real",
                DESCRIPTION + " text", DELETED + " integer default 0", TOKEN + " text", WEB_ID + " text"};

        private Incomes() {}
    }

    public static class Expenses implements Journal {
        public static final String EXPENSES = "expenses";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, EXPENSES);
        public static final String CASH_TYPE_ID = "cash_type_id";
        public static final String EXPENSE_TYPE_ID = "expense_type_id";
        public static final String[] FIELDS = {TIMESTAMP + " integer", PLANNED + " integer",
                CASH_TYPE_ID + " integer", EXPENSE_TYPE_ID + " integer", SUM + " real",
                DESCRIPTION + " text", DELETED + " integer default 0", TOKEN + " text", WEB_ID + " text"};

        private Expenses() {}
    }

    public static class Transfers implements Journal {
        public static final String TRANSFERS = "transfers";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, TRANSFERS);
        public static final String FROM_CASH_TYPE_ID = "from_cash_type_id";
        public static final String TO_CASH_TYPE_ID = "to_cash_type_id";
        public static final String[] FIELDS = {TIMESTAMP + " integer", PLANNED + " integer",
                FROM_CASH_TYPE_ID + " integer", TO_CASH_TYPE_ID + " integer", SUM + " real",
                DESCRIPTION + " text", DELETED + " integer default 0", TOKEN + " text", WEB_ID + " text"};

        private Transfers() {}
    }

    public interface TagLink extends WebColumns {
        public static final String TAG_ID = "tag_id";
    }

    public static class IncomeTags implements TagLink {
        public static final String INCOME_TAGS = "income_tags";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, INCOME_TAGS);
        public static final String INCOME_ID = "income_id";

        public static final String[] FIELDS = {INCOME_ID + " integer", TAG_ID + " integer", TOKEN + " text", WEB_ID + " text"};

        private IncomeTags() {}
    }

    public static class ExpenseTags implements TagLink {
        public static final String EXPENSE_TAGS = "expense_tags";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, EXPENSE_TAGS);
        public static final String EXPENSE_ID = "expense_id";

        public static final String[] FIELDS = {EXPENSE_ID + " integer", TAG_ID + " integer", TOKEN + " text", WEB_ID + " text"};

        private ExpenseTags() {}
    }

    public static class TransferTags implements TagLink {
        public static final String TRANSFER_TAGS = "transfer_tags";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, TRANSFER_TAGS);
        public static final String TRANSFER_ID = "transfer_id";

        public static final String[] FIELDS = {TRANSFER_ID + " integer", TAG_ID + " integer", TOKEN + " text", WEB_ID + " text"};

        private TransferTags() {}
    }

    public static class IncomeJournal implements Journal {
        public static final String INCOME_JOURNAL = "income_journal";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, INCOME_JOURNAL);

        public static final String CASH_TYPE_ID = "cash_type_id";
        public static final String CASH_TYPE_NAME = "cash_type_name";
        public static final String INCOME_TYPE_ID = "income_type_id";
        public static final String INCOME_TYPE_NAME = "income_type_name";


        private static final String SQL =
                "select " +
                "	i.*, " +
                "	ct.name as cash_type_name, " +
                "	it.name as income_type_name " +
                "from " +
                "	incomes i " +
                "	left join cash_types ct on ct._id = i.cash_type_id " +
                "	left join income_types it on it._id = i.income_type_id " +
                "where " +
                "	i.timestamp between ? and ? " +
                "	and i.planned <= ? " +
                "	and i.token = ? " +
                "   and i.deleted = 0 " +
                "order by " +
                "	i.timestamp ";

        static final String[] PARAMS = {SQL, Incomes.CONTENT_URI.toString()};

        private IncomeJournal() {}
    }

    public static class ExpenseJournal implements Journal {
        public static final String EXPENSE_JOURNAL = "expense_journal";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, EXPENSE_JOURNAL);

        public static final String CASH_TYPE_ID = "cash_type_id";
        public static final String CASH_TYPE_NAME = "cash_type_name";
        public static final String EXPENSE_TYPE_ID = "expense_type_id";
        public static final String EXPENSE_TYPE_NAME = "expense_type_name";


        private static final String SQL =
                "select " +
                "	e.*, " +
                "	ct.name as cash_type_name, " +
                "	et.name as expense_type_name " +
                "from " +
                "	expenses e " +
                "	join cash_types ct on ct._id = e.cash_type_id " +
                "	join expense_types et on et._id = e.expense_type_id " +
                "where " +
                "	e.timestamp between ? and ? " +
                "	and e.planned <= ? " +
                "	and e.token = ? " +
                "   and e.deleted = 0 " +
                "order by " +
                "	e.timestamp ";

        static final String[] PARAMS = {SQL, Expenses.CONTENT_URI.toString()};

        private ExpenseJournal() {}
    }

    public static class TransferJournal implements Journal {
        public static final String TRANSFER_JOURNAL = "transfer_journal";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, TRANSFER_JOURNAL);

        public static final String FROM_CASH_TYPE_ID = "from_cash_type_id";
        public static final String FROM_CASH_TYPE_NAME = "from_cash_type_name";
        public static final String TO_CASH_TYPE_ID = "to_cash_type_id";
        public static final String TO_CASH_TYPE_NAME = "to_cash_type_name";


        private static final String SQL =
                "select " +
                "	t.*, " +
                "	fct.name as from_cash_type_name, " +
                "	tct.name as to_cash_type_name " +
                "from " +
                "	transfers t " +
                "	left join cash_types fct on fct._id = t.from_cash_type_id " +
                "	left join cash_types tct on tct._id = t.to_cash_type_id " +
                "where " +
                "	t.timestamp between ? and ? " +
                "	and t.planned <= ? " +
                "	and t.token = ? " +
                "   and t.deleted = 0 " +
                "order by " +
                "	t.timestamp ";

        static final String[] PARAMS = {SQL, Transfers.CONTENT_URI.toString()};

        private TransferJournal() {}
    }

    public static class BallanceReport {

        public static final String BALLANCE_REPORT = "ballance_report";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, BALLANCE_REPORT);

        private static final String SQL =
                "select 10 as gr, -1 as _id, 'Балланс' as name, @bd as old_in_sum, @ed as old_out_sum, @p as old_tr_in_sum, @t as old_tr_out_sum, " +
                "    coalesce((select sum(t.sum) from incomes t where t.timestamp <= @ed and t.planned <= @p and t.token = @t and t.deleted = 0), 0) - " +
                "    coalesce((select sum(t.sum) from expenses t where t.timestamp <= @ed and t.planned <= @p and t.token = @t and t.deleted = 0), 0) as in_sum, " +
                "    null as out_sum, null as tr_in_sum, null as tr_out_sum " +
                " union all " +
                "select " +
                "   11 as gr, " +
                "	ct._id, " +
                "	ct.name, " +
                "	(select sum(t.sum) from incomes t where t.cash_type_id = ct._id and t.timestamp < p.bdate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as old_in_sum, " +
                "	(select sum(t.sum) from expenses t where t.cash_type_id = ct._id and t.timestamp < p.bdate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as old_out_sum, " +
                "	(select sum(t.sum) from transfers t where t.to_cash_type_id = ct._id and t.timestamp < p.bdate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as old_tr_in_sum, " +
                "	(select sum(t.sum) from transfers t where t.from_cash_type_id = ct._id and t.timestamp < p.bdate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as old_tr_out_sum, " +
                "	(select sum(t.sum) from incomes t where t.cash_type_id = ct._id and t.timestamp between p.bdate and p.edate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as in_sum, " +
                "	(select sum(t.sum) from expenses t where t.cash_type_id = ct._id and t.timestamp between p.bdate and p.edate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as out_sum, " +
                "	(select sum(t.sum) from transfers t where t.to_cash_type_id = ct._id and t.timestamp between p.bdate and p.edate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as tr_in_sum, " +
                "	(select sum(t.sum) from transfers t where t.from_cash_type_id = ct._id and t.timestamp between p.bdate and p.edate and t.planned <= p.planned and t.token = p.token and t.deleted = 0) as tr_out_sum " +
                "from " +
                "	cash_types ct " +
                "	join (select @bd as bdate, @ed as edate, @p as planned, @t as token) p on ct.token = p.token " +
                " union all " +
                "select 20 as gr, -1 as _id, 'Приходы' as name, null as old_in_sum, null as old_out_sum, null as old_tr_in_sum, null as old_tr_out_sum, " +
                "    (select sum(t.sum) from incomes t where t.timestamp between @bd and @ed and t.planned <= @p and t.token = @t and t.deleted = 0) as in_sum, " +
                "    null as out_sum, null as tr_in_sum, null as tr_out_sum " +
                " union all " +
                "select " +
                "   21 as gr, " +
                "	it._id, " +
                "	it.name, " +
                "	null as old_in_sum, " +
                "	null as old_out_sum, " +
                "	null as old_tr_in_sum, " +
                "	null as old_tr_out_sum, " +
                "	(select sum(t.sum) from incomes t where t.income_type_id = it._id and t.timestamp between @bd and @ed and t.planned <= @p and t.token = @t and t.deleted = 0) as in_sum, " +
                "	null as out_sum, " +
                "	null as tr_in_sum, " +
                "	null as tr_out_sum " +
                "from " +
                "	income_types it " +
                "where " +
                "   exists(select 1 from incomes e where e.income_type_id = it._id and e.timestamp between @bd and @ed and e.planned <= @p and e.token = @t and e.deleted = 0) " +
                " union all " +
                "select 30 as gr, -1 as _id, 'Расходы' as name, null as old_in_sum, null as old_out_sum, null as old_tr_in_sum, null as old_tr_out_sum, " +
                "    (select sum(t.sum) from expenses t where t.timestamp between @bd and @ed and t.planned <= @p and t.token = @t and t.deleted = 0) as in_sum, " +
                "    null as out_sum, null as tr_in_sum, null as tr_out_sum " +
                " union all " +
                "select " +
                "   31 as gr, " +
                "	et._id, " +
                "	et.name, " +
                "	null as old_in_sum, " +
                "	null as old_out_sum, " +
                "	null as old_tr_in_sum, " +
                "	null as old_tr_out_sum, " +
                "	(select sum(t.sum) from expenses t where t.expense_type_id = et._id and t.timestamp between @bd and @ed and t.planned <= @p and t.token = @t and t.deleted = 0) as in_sum, " +
                "	null as out_sum, " +
                "	null as tr_in_sum, " +
                "	null as tr_out_sum " +
                "from " +
                "	expense_types et " +
                "where " +
                "   exists(select 1 from expenses e where e.expense_type_id = et._id and e.timestamp between @bd and @ed and e.planned <= @p and e.token = @t and e.deleted = 0) " +
                "order by " +
                "	gr, name ";

        static final String[] PARAMS = {SQL, BASE_URI.toString()};

        private BallanceReport() {}
    }

}
