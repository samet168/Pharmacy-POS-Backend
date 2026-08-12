package com.pharmacy.pos.common;

public class TenantContext {
    private static final ThreadLocal<Long> organizationId = new ThreadLocal<>();
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<Long> roleId = new ThreadLocal<>();
    private static final ThreadLocal<Long> branchId = new ThreadLocal<>();

    public static void setOrganizationId(Long orgId) {
        organizationId.set(orgId);
    }

    public static Long getOrganizationId() {
        return organizationId.get();
    }

    public static void setUserId(Long uId) {
        userId.set(uId);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void setRoleId(Long rId) {
        roleId.set(rId);
    }

    public static Long getRoleId() {
        return roleId.get();
    }

    public static void setBranchId(Long bId) {
        branchId.set(bId);
    }

    public static Long getBranchId() {
        return branchId.get();
    }

    public static void clear() {
        organizationId.remove();
        userId.remove();
        roleId.remove();
        branchId.remove();
    }
}
