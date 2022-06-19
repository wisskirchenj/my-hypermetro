package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.NetType;
import de.cofinpro.metro.model.Station;

/**
 * InsertCommand class is used for the /append as well as /add-head commands on the command line.
 * The command has two arguments lineName and stationName.
 */
public class InsertCommand implements LineCommand {

    private final CommandType type;
    private final MetroPrinter metroPrinter;
    private final String lineName;
    private final String stationName;
    private final String time;
    private MetroNet lines;

    public InsertCommand(CommandType type, MetroPrinter printer, String lineName, String stationName, String time) {
        this.type = type;
        this.metroPrinter = printer;
        this.lineName = lineName;
        this.stationName = stationName;
        this.time = time;
    }

    /**
     * inserts the station given by name either at index position 1 (after depot) in case of /add-head
     * or before the last station (depot) in case of /append or /add.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(MetroNet lines) {
        this.lines = lines;
        MetroLine line = lines.get(lineName);
        if (line == null || !time.matches("\\d+")) {
            metroPrinter.printError("Invalid Command");
            return;
        }
        if (type == CommandType.ADD_HEAD) {
            addHead(line);
        } else {
            append(line);
        }
    }

    private void append(MetroLine line) {
        int tailIndex = lines.getType() == NetType.CLASSICAL
                ? line.size() - 1 : line.size();
        Station newStation = line.addStationByName(tailIndex, stationName, Integer.parseInt(time));
        Station previous = line.get(tailIndex - 1);
        newStation.addPrevious(previous);
        previous.addNext(newStation);
    }

    private void addHead(MetroLine line) {
        int headIndex = lines.getType() == NetType.CLASSICAL ? 1 : 0;
        Station newStation = line.addStationByName(headIndex, stationName, Integer.parseInt(time));
        Station next = line.get(headIndex + 1);
        newStation.addNext(next);
        next.addPrevious(newStation);
    }

    @Override
    public CommandType getType() {
        return type;
    }
}
