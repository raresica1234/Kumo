using Microsoft.EntityFrameworkCore.Migrations;

namespace Backend.Migrations
{
    public partial class PermissionRole : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "KumoRoles",
                columns: table => new
                {
                    Id = table.Column<string>(type: "TEXT", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_KumoRoles", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "PathPoints",
                columns: table => new
                {
                    Id = table.Column<string>(type: "TEXT", nullable: false),
                    Path = table.Column<string>(type: "TEXT", nullable: true),
                    IsRoot = table.Column<bool>(type: "INTEGER", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PathPoints", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "KumoUserRoles",
                columns: table => new
                {
                    UserId = table.Column<string>(type: "TEXT", nullable: false),
                    RoleId = table.Column<string>(type: "TEXT", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_KumoUserRoles", x => new { x.RoleId, x.UserId });
                    table.ForeignKey(
                        name: "FK_KumoUserRoles_AspNetUsers_UserId",
                        column: x => x.UserId,
                        principalTable: "AspNetUsers",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_KumoUserRoles_KumoRoles_RoleId",
                        column: x => x.RoleId,
                        principalTable: "KumoRoles",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Permissions",
                columns: table => new
                {
                    RoleId = table.Column<string>(type: "TEXT", nullable: false),
                    PathPointId = table.Column<string>(type: "TEXT", nullable: false),
                    Read = table.Column<bool>(type: "INTEGER", nullable: false),
                    Write = table.Column<bool>(type: "INTEGER", nullable: false),
                    Delete = table.Column<bool>(type: "INTEGER", nullable: false),
                    ModifyRoot = table.Column<bool>(type: "INTEGER", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Permissions", x => new { x.PathPointId, x.RoleId });
                    table.ForeignKey(
                        name: "FK_Permissions_KumoRoles_RoleId",
                        column: x => x.RoleId,
                        principalTable: "KumoRoles",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Permissions_PathPoints_PathPointId",
                        column: x => x.PathPointId,
                        principalTable: "PathPoints",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_KumoUserRoles_UserId",
                table: "KumoUserRoles",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_Permissions_RoleId",
                table: "Permissions",
                column: "RoleId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "KumoUserRoles");

            migrationBuilder.DropTable(
                name: "Permissions");

            migrationBuilder.DropTable(
                name: "KumoRoles");

            migrationBuilder.DropTable(
                name: "PathPoints");
        }
    }
}
