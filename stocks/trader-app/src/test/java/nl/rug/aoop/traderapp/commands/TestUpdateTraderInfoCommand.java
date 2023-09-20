package nl.rug.aoop.traderapp.commands;

import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUpdateTraderInfoCommand {
    private final LocalStockExchange localStockExchange = Mockito.mock(LocalStockExchange.class);
    private final UpdateTraderInfoCommand updateTraderInfoCommand = new UpdateTraderInfoCommand(localStockExchange);

    @Test
    public void testExecute() throws IOException {
        String id = "id";
        String name = "name";
        double funds = 1500.0;
        Map<String, Integer> stockPortfolio = new HashMap<>();
        stockPortfolio.put("comp", 2);

        Map<String, Object> params = new HashMap<>();
        String jsonTraderInfo = "mock";

        NetworkTraderInfo mockSentTraderInfo = Mockito.mock(NetworkTraderInfo.class);
        Mockito.when(mockSentTraderInfo.getId()).thenReturn(id);
        Mockito.when(mockSentTraderInfo.getName()).thenReturn(name);
        Mockito.when(mockSentTraderInfo.getFunds()).thenReturn(funds);
        Mockito.when(mockSentTraderInfo.getStockPortfolio()).thenReturn(stockPortfolio);
        params.put("body", jsonTraderInfo);
        try (MockedStatic<Converter> mockFromJson = Mockito.mockStatic(Converter.class)) {
            mockFromJson.when(() -> Converter.fromJson(jsonTraderInfo, NetworkTraderInfo.class))
                    .thenReturn(mockSentTraderInfo);
            updateTraderInfoCommand.execute(params);
            Mockito.verify(localStockExchange).updateTraderInfo(mockSentTraderInfo);
        }
    }

    @Test
    public void testExecuteNoTraderInfo() {
        Map<String, Object> params = new HashMap<>();
        assertThrows(IOException.class, () -> updateTraderInfoCommand.execute(params));
    }
}
