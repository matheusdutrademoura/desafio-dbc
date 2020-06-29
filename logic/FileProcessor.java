package logic;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import domain.Sale;
import util.FileOperations;

import static util.SimpleLogger.log;

public class FileProcessor {

    private String inFolder;
    private String outFolder;
    private String tmpFolder;

    public FileProcessor(final String in, final String out, final String tmp) {
        this.inFolder = in;
        this.outFolder = out;
        this.tmpFolder = tmp;
    }

    public void process() {

        Set<Path> filesToProcess = FileOperations.list(inFolder);

        filesToProcess.forEach((file) -> {

            try {
                String outputText = generateResume(Files.readAllLines(file));
                FileOperations.write(outputText, FileOperations.outputFilePath(file, outFolder));
                FileOperations.move(file, tmpFolder);

            } catch (IOException e) {
                log(file.getFileName().toString() + ": " + e.getMessage());
            }
        });

        log(filesToProcess.size() + " arquivos processados...");
    }

    private String generateResume(List<String> lines) {

        long numberOfClients = 0;
        long numberOfSellers = 0;
        long mostExpensiveSaleId = 0;
        BigDecimal mostExpensiveSalePrice = new BigDecimal(0);
        String worstSeller = "";
        Map<String, BigDecimal> sellerRevenueMap = new HashMap<>();

        for (String line : lines) {

            String id = line.substring(0, 3);

            if ("001".equals(id)) {
                numberOfClients++;

            } else if ("002".equals(id)) {
                numberOfSellers++;

            } else if ("003".equals(id)) {

                Sale sale = Sale.parseSale(line);

                // Identifica a venda mais cara
                if (sale.totalPrice().compareTo(mostExpensiveSalePrice) == 1) {
                    mostExpensiveSaleId = sale.getId();
                    mostExpensiveSalePrice = sale.totalPrice();
                }

                // Salva o somatório do valor das vendas do vendedor em um mapa
                // no qual a chave é o nome do vendedor
                BigDecimal sellerRevenue = sellerRevenueMap.get(sale.getSeller());

                if (sellerRevenue != null)
                    sellerRevenue = sellerRevenue.add(sale.totalPrice());
                else // primeira ocorrência do vendedor
                    sellerRevenue = sale.totalPrice();

                sellerRevenueMap.put(sale.getSeller(), sellerRevenue);
                // fim da lógica que salva o somatório do valor das vendas do vendedor

            } else continue;
        }

        worstSeller = discoverWorstSellers(sellerRevenueMap);

        return
            "Quantidade de clientes: " + numberOfClients    + "\n" +
            "Quantidade de vendedores: " + numberOfSellers  + "\n" +
            "ID da venda mais cara: " + mostExpensiveSaleId + "\n" +
            "Pior vendedor: " + worstSeller;
    }

    // Pode haver mais de um vendedor com o mesmo valor mínimo de vendas.
    // Neste caso retorna os nomes dos vendedores separados por vírgulas
    private String discoverWorstSellers(Map<String, BigDecimal> sellerRevenueMap) {

        List<BigDecimal> list = new ArrayList<>(sellerRevenueMap.values());
        list.sort(Comparator.naturalOrder());
        BigDecimal min = list.get(0);
        List<String> worstSellers = sellerRevenueMap.entrySet().stream()
            .filter(e -> e.getValue() == min)
            .map(e -> e.getKey())
            .collect(Collectors.toList());

        return String.join(", ", worstSellers);
    }

}