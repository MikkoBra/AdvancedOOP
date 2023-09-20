package nl.rug.aoop.traderapp.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;

import java.io.IOException;
import java.util.Map;

/**
 * Command used for updating information on a specific Trader. Implements Command interface.
 */
@Slf4j
public class UpdateTraderInfoCommand implements Command {
    private final LocalStockExchange localStockExchange;

    /**
     * Basic Constructor.
     *
     * @param localStockExchange LocalStockExchange object that manages interaction between a trader
     *                           and the stock exchange.
     */
    public UpdateTraderInfoCommand(LocalStockExchange localStockExchange) {
        this.localStockExchange = localStockExchange;
    }

    /**
     * Method for executing the updating of a TraderInfo object through field variable localStockExchange.
     *
     * @param params Map containing a String representation of a method parameter as a key, and the actual value of the
     *               intended parameter as a value.
     * @throws IOException When the parameter map was not correctly initialized.
     */
    @Override
    public void execute(Map<String, Object> params) throws IOException {
        if (!params.containsKey("body")) {
            throw new IOException("UpdateTraderInfoCommand was executed without a body");
        }

        String json = (String) params.get("body");
        NetworkTraderInfo receivedTraderInfo = Converter.fromJson(json, NetworkTraderInfo.class);
        localStockExchange.updateTraderInfo(receivedTraderInfo);
    }
}
