package com.l3tplay.crashclaimmap.hook;

import net.crashcraft.crashclaim.CrashClaim;
import net.crashcraft.crashclaim.claimobjects.Claim;
import net.crashcraft.crashclaim.claimobjects.SubClaim;
import net.crashcraft.crashclaim.config.GlobalConfig;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CrashClaimHook {

    public static boolean isWorldEnabled(UUID uuid) {
        return !GlobalConfig.disabled_worlds.contains(uuid);
    }

    public static Collection<Claim> getClaims() {
        return CrashClaim.getPlugin().getDataManager().getClaimCache().asMap().values();
    }

    public static int getSubclaimsSize(Claim claim) {
        List<SubClaim> subclaims = claim.getSubClaims();
        if (subclaims == null || subclaims.size() == 0) {
            return 0;
        }

        return subclaims.size();
    }

    public static String getSubclaims(Claim claim) {
        List<SubClaim> subclaims = claim.getSubClaims();
        if (subclaims == null || subclaims.size() == 0) {
            return "None";
        }

        return subclaims.stream().map(subClaim -> subClaim.getName()).collect(Collectors.joining(", "));
    }

    public static String getEntryMessage(Claim claim) {
        String entryMessage = claim.getEntryMessage();
        return entryMessage == null ? "None" : entryMessage;
    }

    public static String getExitMessage(Claim claim) {
        String exitMessage = claim.getExitMessage();
        return exitMessage == null ? "None" : exitMessage;
    }
}
