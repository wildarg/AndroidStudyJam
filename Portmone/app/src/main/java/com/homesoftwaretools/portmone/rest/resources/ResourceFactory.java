package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class ResourceFactory {


    public static Resource create(String name, ContentValues values) {

        if (name.equals(PortmoneContract.CashTypes.CASH_TYPES)) return new Lib(values);
        if (name.equals(PortmoneContract.IncomeTypes.INCOME_TYPES)) return new Lib(values);
        if (name.equals(PortmoneContract.ExpenseTypes.EXPENSE_TYPES)) return new Lib(values);
        if (name.equals(PortmoneContract.Tags.TAGS)) return new Lib(values);

        if (name.equals(PortmoneContract.Incomes.INCOMES)) return new Income(values);
        if (name.equals(PortmoneContract.Expenses.EXPENSES)) return new Expense(values);
        if (name.equals(PortmoneContract.Transfers.TRANSFERS)) return new Transfer(values);

        if (name.equals(PortmoneContract.IncomeTags.INCOME_TAGS)) return new IncomeTagLink(values);
        if (name.equals(PortmoneContract.ExpenseTags.EXPENSE_TAGS)) return new ExpenseTagLink(values);
        if (name.equals(PortmoneContract.TransferTags.TRANSFER_TAGS)) return new TransferTagLink(values);

        if (name.equals(PortmoneContract.Users.USERS)) return new Notes(values);

        return null;
    }


}
