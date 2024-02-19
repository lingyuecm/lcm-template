import {useEffect, useState} from "react";
import {getRolesApi} from "../../api/roleApi";
import {
    Checkbox,
    createTheme, Dialog, DialogContent,
    DialogTitle, FormControlLabel, FormGroup,
    Pagination,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    ThemeProvider
} from "@mui/material";
import {
    colorBlue,
    colorBlueDark, colorBlueDarker, colorBlueLight, colorGrey64, colorGrey85, colorGreyA6,
    colorGreyD2,
    colorOrange,
    colorOrangeDark,
    colorOrangeDarker, colorOrangeLight, colorSuccess, colorSuccessLight
} from "../../utils/constant";
import styled from "styled-components";
import {allPermissionsApi, grantPermissionsApi, rolePermissionsApi} from "../../api/permissionApi";

const RolePermissionButton = styled.div`
    display: inline-block;
    padding: 0.3rem 1rem;
    border-radius: 0.5rem;
    background-color: ${() => colorOrange};
    box-shadow: 0 0 0.2rem #333333;
    &:hover {
        cursor: pointer;
        background-color: ${() => colorOrangeDark}
    }
    &:active {
        background-color: ${() => colorOrangeDarker}
    }
`

const RoleTotalCount = styled.span`
    float: right;
    margin-top: 1rem;
    font-size: 1.5rem;
    padding: 0.6rem 0;
`

const FullPermission = styled.span`
    color: ${() => colorGrey85};
    font-size: 1.8rem;
`

const HttpMethod = styled.span`
    color: ${props => {
        if (props.method === "GET") {
            return colorSuccess;
        }
        else if (props.method === "PUT") {
            return colorBlue;
        }
        else if (props.method === "POST") {
            return colorOrange;
        }
        else {
            return colorSuccess;
        }
    }};
    font-weight: bold;
    border-width: 0.1rem;
    border-style: solid;
    border-color: ${props => {
        if (props.method === "GET") {
            return colorSuccess;
        }
        else if (props.method === "PUT") {
            return colorBlue;
        }
        else if (props.method === "POST") {
            return colorOrange;
        }
        else {
            return colorSuccess;
        }
    }};
    border-radius: 0.5rem;
    padding: 0.1rem 0.5rem;
    background-color: ${props => {
        if (props.method === "GET") {
            return colorSuccessLight;
        }
        else if (props.method === "PUT") {
            return colorBlueLight;
        }
        else if (props.method === "POST") {
            return colorOrangeLight;
        }
        else {
            return colorSuccessLight;
        }
    }};
`

const DialogButtonWrapper = styled.div`
    padding: 1rem 2rem;
    border-top: ${() => "1px solid " + colorGreyD2};
`

const PermissionsDialogButton = styled.div`
    display: inline-block;
    float: right;
    width: fit-content;
    padding: 0.6rem 1.6rem;
    border-radius: 0.5rem;
    &:hover {
        cursor: pointer;
    }
`

const ConfirmationButton = styled(PermissionsDialogButton)`
    background-color: ${() => colorBlue};
    &:hover {
        background-color: ${() => colorBlueDark}
    }
    &:active {
        background-color: ${() => colorBlueDarker}
    }
`

const CancellationButton = styled(PermissionsDialogButton)`
    background-color: ${() => colorGreyA6};
    &:hover {
        background-color: ${() => colorGrey85}
    }
    &:active {
        background-color: ${() => colorGrey64}
    }
`

const roleTableTheme = createTheme({
    components: {
        MuiTableHead: {
            styleOverrides: {
                root: {
                    backgroundColor: colorBlue
                }
            }
        },
        MuiTableCell: {
            styleOverrides: {
                head: {
                    fontSize: "1.8rem"
                }
            }
        },
        MuiTableBody: {
            styleOverrides: {
                root: {
                    borderLeft: "1px solid " + colorGreyD2,
                    borderRight: "1px solid " + colorGreyD2
                }
            }
        },
        MuiPaginationItem: {
            styleOverrides: {
                root: {
                    fontSize: "1.5rem",
                    "&.Mui-selected": {
                        backgroundColor: colorBlue,
                        color: colorOrangeLight,
                        fontWeight: 700
                    },
                    "&.Mui-selected:hover": {
                        backgroundColor: colorBlueDark
                    }
                },
                icon: {
                    width: "2rem",
                    height: "2rem"
                }
            }
        }
    }
});

const rolePermissionsTheme = createTheme({
    components: {
        MuiTypography: {
            styleOverrides: {
                root: {
                    fontSize: "1.5rem"
                }
            }
        },
        MuiDialogTitle: {
            styleOverrides: {
                root: {
                    borderBottom: "1px solid #D2D2D2",
                    padding: "1rem 2rem"
                }
            }
        },
        MuiDialogContent: {
            styleOverrides: {
                root: {
                    width: "50rem",
                    height: "30rem"
                }
            }
        }
    }
})

export default function RoleManagement() {
    const [roles, setRoles] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [pages, setPages] = useState(0);
    const [pageNo, setPageNo] = useState(1);
    const [currentRoleId, setCurrentRoleId] = useState(0);
    const [allPermissionsVisible, setAllPermissionsVisible] = useState(false);
    const [allPermissions, setAllPermissions] = useState([{
        permissionId: 1,
        httpMethod: "GET",
        permissionUrl: "/permission/url"
    },
        {
            permissionId: 2,
            httpMethod: "PUT",
            permissionUrl: "/permission/url"
        },
        {
            permissionId: 3,
            httpMethod: "POST",
            permissionUrl: "/permission/url"
        }]);
    const [rolePermissions, setRolePermissions] = useState([]);
    let pageSize = 10;

    function searchForRoles() {
        const params = {};
        params.pageNo = pageNo;
        params.pageSize = pageSize;

        getRolesApi(params)
            .then(response => {
                setTotalCount(response["resultBody"]["totalCount"]);
                setPages(Math.floor((response["resultBody"]["totalCount"] + pageSize - 1) / pageSize));
                setRoles(response["resultBody"]["dataList"]);
            }).catch(() => {});
    }

    function onPermissionsClick(roleId) {
        setCurrentRoleId(roleId);
        setAllPermissionsVisible(true);
    }

    function getAllPermissions() {
        allPermissionsApi().then(response => {
            setAllPermissions(response["resultBody"]);
            rolePermissionsApi(currentRoleId).then(response => {
                setRolePermissions(response["resultBody"].map(p => p.permissionId));
            });
        });
    }

    function onPermissionToggled(permissionId, checked) {
        if (checked) {
            setRolePermissions(rolePermissions.concat(permissionId));
        }
        else {
            let index = rolePermissions.indexOf(permissionId);
            setRolePermissions(rolePermissions.slice(0, index).concat(rolePermissions.slice(index + 1)));
        }
    }

    function grantPermissions() {
        grantPermissionsApi(currentRoleId, rolePermissions)
            .then(() => {
                setAllPermissionsVisible(false);
            }).catch(() => {});
    }

    useEffect(() => {
        searchForRoles();
    }, [pageNo]);

    useEffect(() => {
        if (allPermissionsVisible) {
            getAllPermissions();
        }
        else {
            setCurrentRoleId(0);
            setAllPermissions([]);
            setRolePermissions([]);
        }
    }, [allPermissionsVisible])

    return <div>
        <ThemeProvider theme={roleTableTheme}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell style={{borderTopLeftRadius: "1rem", borderRight: "1px solid #B2B2B2"}}
                                   align={"center"}>Name</TableCell>
                        <TableCell style={{borderTopRightRadius: "1rem"}} align={"center"}>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {roles.map((role, index) => {
                        return <TableRow key={"RoleRow-" + index}>
                            <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid " + colorGreyD2}} align={"center"}>{role.roleName}</TableCell>
                            <TableCell style={{fontSize: "1.5rem"}} align={"center"}>
                                <RolePermissionButton onClick={() => onPermissionsClick(role.roleId)}>Permissions</RolePermissionButton>
                            </TableCell>
                        </TableRow>;
                    })}
                </TableBody>
            </Table>
            <Pagination style={{float: "right", marginTop: "1rem"}} count={pages} page={pageNo}
                        onChange={(e, page) => {setPageNo(page)}}/>
            <RoleTotalCount>{totalCount} in total</RoleTotalCount>
        </ThemeProvider>
        <Dialog open={allPermissionsVisible}>
            <ThemeProvider theme={rolePermissionsTheme}>
                <DialogTitle>Grant permissions</DialogTitle>
                <DialogContent>
                    <FormGroup>
                        {allPermissions.map((permission, index) => {
                            return <FormControlLabel
                                key={"Role-" + index}
                                control={<Checkbox checked={rolePermissions.includes(permission.permissionId)}/>}
                                label={<FullPermission>
                                    <HttpMethod method={permission.httpMethod}>{permission.httpMethod}</HttpMethod>
                                    &nbsp;
                                    {permission.permissionUrl}
                            </FullPermission>}
                                onChange={e => onPermissionToggled(permission.permissionId, e.target.checked)}/>
                        })}
                    </FormGroup>
                </DialogContent>
            </ThemeProvider>
            <DialogButtonWrapper>
                <CancellationButton onClick={() => setAllPermissionsVisible(false)}>Cancel</CancellationButton>
                <ConfirmationButton style={{marginRight: "1rem"}}
                                    onClick={() => grantPermissions()}>OK
                </ConfirmationButton>
            </DialogButtonWrapper>
        </Dialog>
    </div>
}