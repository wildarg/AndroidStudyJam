package com.homesoftwaretools.portmone.rest;
/*
 * Created by Wild on 09.05.2015.
 */

import com.homesoftwaretools.portmone.domain.User;
import com.homesoftwaretools.portmone.rest.resources.Expense;
import com.homesoftwaretools.portmone.rest.resources.ExpenseTagLink;
import com.homesoftwaretools.portmone.rest.resources.Income;
import com.homesoftwaretools.portmone.rest.resources.IncomeTagLink;
import com.homesoftwaretools.portmone.rest.resources.Lib;
import com.homesoftwaretools.portmone.rest.resources.Resource;
import com.homesoftwaretools.portmone.rest.resources.Transfer;
import com.homesoftwaretools.portmone.rest.resources.TransferTagLink;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface PortmoneApi {

    @POST("/api/v1/users")
    public void createUser(@Body User user, Callback<Response> callback);

    @GET("/api/v1/users")
    public void getProfile(@Query("login") String login, @Query("password") String password, Callback<User> callback);

    @POST("/api/v1/{path}")
    public Response insert(@Path("path") String path, @Body Resource resource);

    @PUT("/api/v1/{path}/{id}")
    public Response update(@Path("path") String path, @Path("id") String id, @Body Resource resource);

    @PUT("/api/v1/{path}")
    public Response update(@Path("path") String path, @Body Resource resource);

    @DELETE("/api/v1/{path}/{id}")
    public Response delete(@Path("path") String path, @Path("id") String id);

    @GET("/api/v1/{path}")
    public List<Lib> getLib(@Path("path") String path);

    @GET("/api/v1/incomes")
    public List<Income> getIncomes();

    @GET("/api/v1/expenses")
    public List<Expense> getExpenses();

    @GET("/api/v1/transfers")
    public List<Transfer> getTransfers();

    @GET("/api/v1/incometags")
    public List<IncomeTagLink> getIncomeTags();

    @GET("/api/v1/expensetags")
    public List<ExpenseTagLink> getExpenseTags();

    @GET("/api/v1/transfertags")
    public List<TransferTagLink> getTransferTags();





}
