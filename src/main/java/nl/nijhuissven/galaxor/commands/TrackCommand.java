package nl.nijhuissven.galaxor.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.CommandCompletion;
import nl.nijhuissven.galaxor.util.ChatUtils;
import org.bukkit.command.CommandSender;
import nl.nijhuissven.galaxor.Galaxor;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.influxdb.query.FluxRecord;
import org.bukkit.Bukkit;

import java.util.List;

@CommandAlias("galaxor")
public class TrackCommand extends BaseCommand {

    @Subcommand("track")
    @CommandCompletion("@nothing")
    public void onTrack(CommandSender sender, String field) {
        Bukkit.getScheduler().runTaskAsynchronously(Galaxor.instance(), () -> {
            InfluxDBClient influx = Galaxor.db().getClient();
            String bucket = Galaxor.instance().configuration().influxBucket();
            String flux = String.format("from(bucket: \"%s\")\n  |> range(start: -24h)\n  |> filter(fn: (r) => r._measurement == \"galaxor_events\" and r._field == \"%s\")\n  |> sum()", bucket, field);
            double value = 0;
            String error = null;
            try {
                QueryApi queryApi = influx.getQueryApi();
                List<FluxTable> tables = queryApi.query(flux);
                for (FluxTable table : tables) {
                    for (FluxRecord record : table.getRecords()) {
                        Object v = record.getValueByKey("_value");
                        if (v instanceof Number) {
                            value += ((Number) v).doubleValue();
                        }
                    }
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            final double result = value;
            final String err = error;
            Bukkit.getScheduler().runTask(Galaxor.instance(), () -> {
                if (err != null) {
                    sender.sendMessage(ChatUtils.prefixed("<red>Error fetching data!"));
                    sender.sendMessage(ChatUtils.prefixed("<red>" + err));
                } else {
                    sender.sendMessage(ChatUtils.prefixed("Tracked value for '" + field + "' in the last 24h: §e" + result));
                }
            });
        });
    }
} 