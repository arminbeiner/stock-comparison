package ch.ibw.nds.appl2017.service;

import ch.ibw.nds.appl2017.model.ComparisonInput;
import ch.ibw.nds.appl2017.model.Stock;
import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static java.util.Calendar.DECEMBER;

@RunWith(OleasterRunner.class)
public class ComparisonControllerTest {{

    String stockString1 = "NESN";
    String stockString2 = "GOOGL";
    String stockString3 = "AI";

    List<String> stockStringList = new ArrayList<>();
    stockStringList.add((stockString1));
    stockStringList.add((stockString2));

    Stock stock1 = Stock.createStock(stockString1);
    Stock stock2 = Stock.createStock(stockString2);
    List<Stock> stockList = new ArrayList();
    stockList.add(stock1);
    stockList.add(stock2);

    String fromDateString = "20170101";
    String toDateString = "20171231";


    describe("Test Stock/ComparisonInput Building Methods", () -> {
        it("should bundle Elements for a ComparisonInput Object", () -> {
            ComparisonController comparisonController = new ComparisonController();
            ComparisonInput comparisonInput = ComparisonInput.createComparisonInput(stockStringList, fromDateString, toDateString);
            expect(comparisonInput.getStocks().size()).toEqual(stockStringList.size());
            expect(comparisonInput.getStocks().get(0).getSymbol()).toEqual(stockString1);
        });
    });

    describe("Test Service Method", () -> {
        it("should return http response code 200", () -> {
            ComparisonController comparisonController = new ComparisonController();
            Response response = comparisonController.getPerformance(stockStringList, fromDateString, toDateString);
            System.out.println(response.getStatus());
            System.out.println(response.toString());
            System.out.println(response.getEntity().toString());
            expect(response.getStatus()).toEqual(200);
        });

    });

}}