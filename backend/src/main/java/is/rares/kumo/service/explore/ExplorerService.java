package is.rares.kumo.service.explore;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.explore.ExplorerErrorCodes;
import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.mapping.explore.ExplorerMapping;
import is.rares.kumo.model.explore.ExplorerFileModel;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.service.UserService;
import is.rares.kumo.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ExplorerService {
    private final PathPointService pathPointService;
    private final ExplorationRoleService explorationRoleService;
    private final UserService userService;
    private final ExplorerMapping explorerMapping;

    // TODO: Don't display folders which don't have a read permission in linux

    public List<ExplorerFileModel> explore(String path, CurrentUser currentUser) {
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        log.info("User {} is exploring path: {}", currentUser.getUsername(), decodedPath);
        if (decodedPath.isEmpty()) {
            var pathPoints = getRootPathPoints(currentUser);
            pathPoints.sort(Comparator.comparing(ExplorerFileModel::getName));
            return pathPoints;
        }
        return explorePath(decodedPath, currentUser);
    }

    private List<ExplorerFileModel> explorePath(String path, CurrentUser currentUser) {
        var isUserOwner = userService.isUserOwner(currentUser);

        if (isUserOwner)
            return explorePathForOwner(path);

        return explorePathForUser(path, currentUser);
    }

    private List<ExplorerFileModel> explorePathForUser(String path, CurrentUser currentUser) {
        var roles = explorationRoleService.getByUserUuid(currentUser.getId());
        var permissionList = roles.stream().flatMap(role -> role.getPermissions().stream()).toList();

        // Ensure root path point exists
        var rootPermission = permissionList.stream()
                .filter(permission -> permission.isRead() && permission.getPathPoint().isRoot())
                .filter(permission -> path.startsWith(permission.getPathPoint().getPath())
                        || path.startsWith(permission.getPathPoint().getPath().substring(1)))
                .findFirst()
                .orElseThrow(() -> new KumoException(ExplorerErrorCodes.NOT_FOUND));

        String realPath = FileUtils.getRealPath(rootPermission.getPathPoint().getPath(), path);

        // Check if the current path is blacklisted
        var currentPathIsBlacklisted = permissionList.stream()
                .anyMatch(permission ->
                        realPath.startsWith(permission.getPathPoint().getPath()) && !permission.isRead());

        if (currentPathIsBlacklisted)
            throw new KumoException(ExplorerErrorCodes.NOT_FOUND);

        // Get path points that can come in relevant (needed for black listing directories)
        var relevantPermissions = permissionList.stream()
                .filter(permission ->
                        realPath.startsWith(permission.getPathPoint().getPath()) || permission.getPathPoint().getPath().startsWith(realPath))
                .toList();

        File directory = new File(realPath);
        if (!directory.exists() || !directory.isDirectory())
            throw new KumoException(ExplorerErrorCodes.NOT_FOUND);

        File[] contents = directory.listFiles();
        var files = applyBlacklist(applySorting(contents), relevantPermissions);

        return explorerMapping.mapFilesToModelForUser(files, rootPermission, relevantPermissions);
    }

    private List<File> applyBlacklist(List<File> contents, List<Permission> permissions) {
        return contents.stream()
                .filter(file -> isPathBlacklisted(file, permissions)).toList();
    }

    private boolean isPathBlacklisted(File file, List<Permission> permissions) {
        return permissions.stream()
                        .filter(pathPoint -> pathPoint.getPathPoint().getPath().equals(file.getPath()))
                        .filter(permission -> !permission.isRead())
                        .findFirst().isEmpty();
    }

    private List<ExplorerFileModel> explorePathForOwner(String path) {
        var pathPoints = pathPointService.getAllRootPathPoints();
        // Ensure a path point for the current path exists

        // Check path point starts with path or remove the leading slash
        var rootPathPoint = pathPoints.stream()
                .filter(pathPoint -> path.startsWith(pathPoint.getPath()) || path.startsWith(pathPoint.getPath().substring(1)))
                .findFirst()
                .orElseThrow(() -> new KumoException(ExplorerErrorCodes.NOT_FOUND));

        String realPath = FileUtils.getRealPath(rootPathPoint.getPath(), path);


        File directory = new File(realPath);
        if (!directory.exists() || !directory.isDirectory())
            throw new KumoException(ExplorerErrorCodes.NOT_FOUND);

        File[] contents = directory.listFiles();
        var files = applySorting(contents);
        return explorerMapping.mapFilesToModelForOwner(files);
    }

    private List<File> applySorting(File[] files) {
        return Arrays.stream(files).sorted((file1, file2) -> {
            var isDir1 = file1.isDirectory();
            var isDir2 = file2.isDirectory();
            if (isDir1 && !isDir2)
                return -1;
            else if (!isDir1 && isDir2)
                return 1;
            else return file1.getName().compareToIgnoreCase(file2.getName());
        }).filter(File::canRead).toList();
    }

    private List<ExplorerFileModel> getRootPathPoints(CurrentUser currentUser) {
        var isUserOwner = userService.isUserOwner(currentUser);

        if (isUserOwner)
            return getRootPathPointsForOwner();

        return getRootPathPointsForUser(currentUser);
    }

    private List<ExplorerFileModel> getRootPathPointsForUser(CurrentUser currentUser) {
        var roles = explorationRoleService.getByUserUuid(currentUser.getId());
        var rootPathPermissions = roles.stream().flatMap(role -> role.getPermissions().stream())
                .filter(permission -> permission.isRead() && permission.getPathPoint().isRoot())
                .toList();

        return explorerMapping.mapPermissionsToModel(rootPathPermissions);
    }

    private List<ExplorerFileModel> getRootPathPointsForOwner() {
        var pathPoints = pathPointService.getAllRootPathPoints();
        // TODO: decide if want to filter out root path points or not
        //  based on the ones that are in subdirectories of other root ones

        return explorerMapping.mapPathPointsToModel(pathPoints);
    }

    public boolean canAccessPath(String path, CurrentUser currentUser) {
        var isUserOwner = userService.isUserOwner(currentUser);

        if (isUserOwner)
            return canOwnerAccessPath(path);

        var roles = explorationRoleService.getByUserUuid(currentUser.getId());
        var permissionList = roles.stream().flatMap(role -> role.getPermissions().stream()).toList();

        // Ensure root path point exists
        var rootPermissionOptional = permissionList.stream()
                .filter(permission -> permission.isRead() && permission.getPathPoint().isRoot())
                .filter(permission -> path.startsWith(permission.getPathPoint().getPath())
                        || path.startsWith(permission.getPathPoint().getPath().substring(1)))
                .findFirst();

        if (rootPermissionOptional.isEmpty())
            return false;

        String realPath = FileUtils.getRealPath(rootPermissionOptional.get().getPathPoint().getPath(), path);

        // Check if the current path is blacklisted
        var currentPathIsBlacklisted = permissionList.stream()
                .anyMatch(permission ->
                        realPath.startsWith(permission.getPathPoint().getPath()) && !permission.isRead());

        if (currentPathIsBlacklisted)
            return false;

        return new File(realPath).exists();
    }


    private boolean canOwnerAccessPath(String path) {
        var pathPoints = pathPointService.getAllRootPathPoints();
        // Ensure a path point for the current path exists

        // Check path point starts with path or remove the leading slash
        var rootPathPointOptional = pathPoints.stream()
                .filter(pathPoint -> path.startsWith(pathPoint.getPath()) || path.startsWith(pathPoint.getPath().substring(1)))
                .findFirst();

        if (rootPathPointOptional.isEmpty())
            return false;

        String realPath = FileUtils.getRealPath(rootPathPointOptional.get().getPath(), path);

        return new File(realPath).exists();
    }
}
