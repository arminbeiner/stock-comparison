package ch.ibw.nds.appl2017.service;

import ch.ibw.nds.appl2017.model.ComparisonInput;
import ch.ibw.nds.appl2017.model.Stock;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;

@Path("/comparison")
public class ComparisonController {

    // V2 : hier eine unbestimmte Anzahl Stocks, ist so vom Design her eher fuer zuk. Anwendungen offen
    // http://localhost:8080/stock-comparison-1.0-SNAPSHOT/rest/comparison/performance?stock=NESN&stock=GOOGL&stock=ORCL&stock=LISN&dateFrom=20130313&dateTo=20171231
    // http://localhost:8080/stock-comparison-1.0-SNAPSHOT/rest/comparison/correlation?stock=NESN&stock=GOOGL&stock=ORCL&stock=LISN&dateFrom=20130313&dateTo=20171231

    @Path("/correlation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response getCorrelation(
            @QueryParam("stock") final List<String> stockSymbols,
            @NotNull
            @Pattern(regexp = Const.REST_API_DATEFORMAT_REGEX_PATTERN)
            @QueryParam("dateFrom") String fromDateString,
            @NotNull
            @Pattern(regexp = Const.REST_API_DATEFORMAT_REGEX_PATTERN)
            @QueryParam("dateTo") String toDateString) {

        ComparisonInput comparisonInput = buildComparisonInput(stockSymbols, fromDateString, toDateString);

        // todo call berechnung

        // todo hier natuerlich comparisonOutput
        return Response.status(200).entity(comparisonInput).build();
    }

    @Path("/performance")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response getPerformance(
            @Valid
            @QueryParam("stock") final List<String> stockSymbols,
            @NotNull
            @Pattern(regexp = "\\d{4}\\d{2}\\d{2}")
            @QueryParam("dateFrom") String fromDateString,
            @NotNull
            @Pattern(regexp = "\\d{4}\\d{2}\\d{2}")
            @QueryParam("dateTo") String toDateString) {

        // todo testklasse input val

        ComparisonInput comparisonInput = buildComparisonInput(stockSymbols, fromDateString, toDateString);

        // todo call berechnung

        // todo hier natuerlich comparisonOutput
        return Response.status(200).entity(comparisonInput).build();
    }


    public ComparisonInput buildComparisonInput(List<String> stockSymbols, String fromDateString, String toDateString) {
        Date dateFrom = getDateFromApiDateString(fromDateString);
        Date dateTo = getDateFromApiDateString(toDateString);
        List<Stock> stockList = convertToStockList(stockSymbols);

        ComparisonInput comparisonInput = ComparisonInput.createComparisonInput(stockList,dateFrom,dateTo);
        System.out.println(comparisonInput.toString());
        return comparisonInput;
    }

    public static List convertToStockList(List<String> stockSymbols) {
        List<Stock> stockList = new ArrayList();
        for (String stockSymbol: stockSymbols) {
            stockList.add(Stock.createStock(stockSymbol));
            // todo der validator funktioniert nicht
            Validator validator = Validation
                    .buildDefaultValidatorFactory()
                    .getValidator();
            validator.validate(Stock.createStock(stockSymbol));
        }
        return stockList;
    }

    public static Date getDateFromApiDateString(String dateString) {
        try {
            return Const.REST_API_DATEFORMAT.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getStackTrace()); // todo exceptionhandling
        }
        return null; // todo null ist nicht gut
    }



}