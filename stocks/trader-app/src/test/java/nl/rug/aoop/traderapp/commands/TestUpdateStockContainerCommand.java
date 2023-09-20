package nl.rug.aoop.traderapp.commands;

import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUpdateStockContainerCommand {

    private LocalStockExchange localStockExchange;
    private UpdateStockContainerCommand updateStockContainerCommand;

    @BeforeEach
    public void setUp() {
        localStockExchange = Mockito.mock(LocalStockExchange.class);
        updateStockContainerCommand = new UpdateStockContainerCommand(localStockExchange);
    }

    @Test
    public void testExecute() throws IOException {
        Map<String, Object> params = new HashMap<>();
        String jsonStockContainer = "mock";
        NetworkStockContainer mockSentStockContainer = Mockito.mock(NetworkStockContainer.class);
        Map<String, NetworkStock> mockStocks = new HashMap<>();
        NetworkStock mockStock = Mockito.mock(NetworkStock.class);
        mockStocks.put("key", mockStock);
        params.put("body", jsonStockContainer);
        Mockito.when(mockSentStockContainer.getStocks()).thenReturn(mockStocks);
        try (MockedStatic<Converter> mockFromJson = Mockito.mockStatic(Converter.class)) {
            mockFromJson.when(() -> Converter.fromJson(jsonStockContainer, NetworkStockContainer.class))
                    .thenReturn(mockSentStockContainer);
            updateStockContainerCommand.execute(params);
            Mockito.verify(localStockExchange).updateStockContainer(mockSentStockContainer);
        }
    }

    @Test
    public void testExecuteNoTraderInfo() {
        Map<String, Object> params = new HashMap<>();
        assertThrows(IOException.class, () -> updateStockContainerCommand.execute(params));
    }

}
