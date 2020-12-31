package tournament;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Class containing the methods for interacting with the UI.
 */
public class Server {

    private static final int PORT = 8080;
    private static final int OK = 200;
    private static final int NOT_FOUND = 404;
    private final HttpServer server;
    private final Tournament tournament;
    
    /*
     * AF: f(server, tournament) -> Server which provides the communication between the web UI and 
     *   the backend, where server is the HttpServer object handling the contexts and tournament 
     *   is the object providing the methods to interact with the tournaments
     */

    Server() throws IOException, SQLException {
        // Initialize the server
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());
        
        // Initialize the tournament
        tournament = new Tournament();
        // Add contexts
        server.createContext("/table", this::tableHandler);
        server.createContext("/rounds", this::roundsHandler);
        server.createContext("/matches", this::matchesHandler);
        server.createContext("/enter", this::enterHandler);
        server.createContext("/remove", this::removeHandler);
        server.createContext("/create", this::createHandler);
    }
    
    /**
     * Start the server.
     */
    void start() {
        server.start();
    }
    
    /**
     * Stop the server.
     */
    void stop() {
        server.stop(0);
    }
    
    /**
     * Handle the request to /table?tournament=<tournament> by responding with the table in 
     * the following format:
     * 
     * response ::= (row \n)*
     * row ::= name ",' w "," d "," l "," sf "," sa "," sd "," p
     * 
     * @param exchange
     * @throws IOException 
     * @throws SQLException 
     */
    private void tableHandler(HttpExchange exchange) throws IOException {
        // Parse the args
        final Map<String, String> args = getArgs(exchange);
        try {
            // Get the table
            String table = tournament.getTable(args.get("tournament"));
            // Send the table
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(OK, 0);
            writeResponse(exchange, table);
            exchange.close();
        } catch (Exception e) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(NOT_FOUND, 0);
            exchange.close();
        }
    }
    
    /**
     * Handle the request to /rounds?tournament=<tournament> by responding with the names of the 
     * rounds in the tournament in the following format:
     * 
     * response ::= name ("," name)*
     * 
     * @param exchange
     * @throws IOException
     */
    private void roundsHandler(HttpExchange exchange) throws IOException {
        // Parse the args
        final Map<String, String> args = getArgs(exchange);
        try {
            // Get the rounds
            String rounds = tournament.getRoundNames(args.get("tournament"));
            // Send the rounds
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(OK, 0);
            writeResponse(exchange, rounds);
            exchange.close();
        } catch (Exception e) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(NOT_FOUND, 0);
            exchange.close();
        }
    }
    
    /**
     * Handle the request to /rounds?tournament=<tournament>&round=<round> by responding with the 
     * matches in the round in the following format:
     * 
     * response ::= match ("\n" match)*
     * match ::= home "," home_score "," away_score "," away
     * 
     * @param exchange
     * @throws IOException
     */
    private void matchesHandler(HttpExchange exchange) throws IOException {
        // Parse the args
        final Map<String, String> args = getArgs(exchange);
        try {
            // Get the table
            String matches = tournament.getMatchesByRound(args.get("tournament"), args.get(
                    "round"));
            // Send the table
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(OK, 0);
            writeResponse(exchange, matches);
            exchange.close();
        } catch (Exception e) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(NOT_FOUND, 0);
            exchange.close();
        }
    }
    
    /**
     * Handle the request to /enter?tournament=<tournament>&round=<round>&game=<game>
     * &home=<homeScore>&away=<awayScore> by entering the score.
     * @param exchange
     * @throws IOException
     */
    private void enterHandler(HttpExchange exchange) throws IOException {
        // Parse the args
        final Map<String, String> args = getArgs(exchange);
        try {
            // Enter the score
            tournament.enterScore(args.get("tournament"), args.get("round"), 
                    Integer.valueOf(args.get("game")), 
                    Integer.valueOf(args.get("home")), 
                    Integer.valueOf(args.get("away")));
            // Send the response
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(OK, 0);
            exchange.close();
        } catch (Exception e) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(NOT_FOUND, 0);
            exchange.close();
        }
    }

    /**
     * Handle the request to /remove?tournament=<tournament>&round=<round>&game=<game> by removing 
     * the score if it exists
     * @param exchange
     * @throws IOException
     */
    private void removeHandler(HttpExchange exchange) throws IOException {
        // Parse the args
        final Map<String, String> args = getArgs(exchange);
        try {
            // Remove the score
            tournament.removeScoreIfExists(args.get("tournament"), args.get("round"), 
                    Integer.valueOf(args.get("game")));
            // Send the response
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(OK, 0);
            exchange.close();
        } catch (Exception e) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(NOT_FOUND, 0);
            exchange.close();
        }
    }
    
    /**
     * Handle the request to /create?tournament=<tournament>&teams=<teams> by creating a 
     * tournament with the teams. Responds with "success" if successful.
     * @param exchange
     * @throws IOException
     */
    private void createHandler(HttpExchange exchange) throws IOException {
        // Parse the args
        final Map<String, String> args = getArgs(exchange);
        try {
            // Create the tournament
            tournament.create(args.get("tournament"), Arrays.asList(args.get("teams").split(",")));
            // Send the response
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(OK, 0);
            writeResponse(exchange, "success");
            exchange.close();
        } catch (Exception e) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(NOT_FOUND, 0);
            exchange.close();
        }
    }
    
    /**
     * Parse args from a request.
     * @param exchange
     * @return args
     */
    private Map<String, String> getArgs(HttpExchange exchange) {
        final String[] queryList = exchange.getRequestURI().getQuery().split("&");
        final Map<String, String> args = new HashMap<String, String>();
        for (int i = 0; i < queryList.length; i++) {
            final String[] query = queryList[i].split("=");
            args.put(query[0], query[1]);
        }
        return args;
    }

    /**
     * Write a response to an exchange.
     * @param exchange
     * @param response
     */
    private void writeResponse(HttpExchange exchange, String response) {
        OutputStream body = exchange.getResponseBody();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(body, StandardCharsets.UTF_8), true);
        out.print(response);
        out.flush();
    }
    
    public static void main(String[] args) throws IOException, SQLException {
        new Server().start();
    }
}
