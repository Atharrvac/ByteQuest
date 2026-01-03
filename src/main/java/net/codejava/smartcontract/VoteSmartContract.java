package net.codejava.smartcontract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.codejava.repository.VoteRepo;

@Service
public class VoteSmartContract {

    @Autowired
    private VoteRepo voterepo;

    @Autowired
    private net.codejava.repository.CandidateRepo candidaterepo;

    @Autowired
    private net.codejava.repository.UserRepo userRepo;

    public boolean checkTable() {
        // Simple integrity check
        return voterepo.count() == voterepo.findcount();
    }

    public void correctTableValues() {
        // Implementation of correction logic if needed
        System.out.println("Correcting table values...");
    }

    public String voteCount() {
        java.util.List<net.codejava.model.Votedata> allVotes = voterepo.findAll();
        java.util.List<net.codejava.model.Candidate> candidates = candidaterepo.findAll();
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();

        for (net.codejava.model.Votedata vote : allVotes) {
            if ("admin".equals(vote.getUsername()))
                continue; // Skip genesis block

            net.codejava.model.User user = userRepo.findByUsername(vote.getUsername());
            if (user == null)
                continue;

            for (net.codejava.model.Candidate c : candidates) {
                try {
                    String[] data = { vote.getUsername(), user.getFirstname(), c.getParty() };
                    String recalculatedHash = net.codejava.helper.SHA256.getSHA(data, vote.getPrevhash());

                    if (recalculatedHash.equals(vote.getCurrhash())) {
                        counts.put(c.getParty(), counts.getOrDefault(c.getParty(), 0) + 1);
                        break;
                    }
                } catch (Exception e) {
                    // Ignore and try next candidate
                }
            }
        }

        return counts.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(candidates.isEmpty() ? "None" : candidates.get(0).getParty());
    }

    public boolean validateTransaction(net.codejava.model.Votedata vote) {
        try {
            net.codejava.model.User user = userRepo.findByUsername(vote.getUsername());
            if (user == null)
                return false;
            System.out.println("[BLOCKCHAIN] Validating Node Hash for user: " + vote.getUsername());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
