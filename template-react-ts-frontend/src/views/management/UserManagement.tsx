import {
    Checkbox, createTheme,
    Dialog, DialogContent, DialogTitle,
    FormControlLabel,
    FormGroup,
    Pagination,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow, ThemeProvider
} from "@mui/material";
import React, {useEffect, useState} from "react";
import {getUsersApi} from "../../api/userApi";
import {getAllRolesApi, getUserRolesApi, grantRolesApi} from "../../api/roleApi";
import styled from "styled-components";
import {
    colorBlue,
    colorBlueDark, colorBlueDarker, colorGrey64, colorGrey85, colorGreyA6,
    colorGreyD2,
    colorOrange,
    colorOrangeDark,
    colorOrangeDarker, colorOrangeLight
} from "../../utils/constant";
import {BizUserDto, ConfRoleDto} from "../../model/dto";
import {GetUsersRequest} from "../../model/request";

const DialogButtonWrapper = styled.div`
    padding: 1rem 2rem;
    border-top: ${() => "1px solid " + colorGreyD2};
`

const UserTotalCount = styled.span`
    float: right;
    margin-top: 1rem;
    font-size: 1.5rem;
    padding: 0.6rem 0;
`

const UserRolesButton = styled.div`
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

const RolesDialogButton = styled.div`
    display: inline-block;
    float: right;
    width: fit-content;
    padding: 0.6rem 1.6rem;
    border-radius: 0.5rem;
    &:hover {
        cursor: pointer;
    }
`

const ConfirmationButton = styled(RolesDialogButton)`
    background-color: ${() => colorBlue};
    &:hover {
        background-color: ${() => colorBlueDark}
    }
    &:active {
        background-color: ${() => colorBlueDarker}
    }
`

const CancellationButton = styled(RolesDialogButton)`
    background-color: ${() => colorGreyA6};
    &:hover {
        background-color: ${() => colorGrey85}
    }
    &:active {
        background-color: ${() => colorGrey64}
    }
`

const userTableTheme = createTheme({
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

const userRolesTheme = createTheme({
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

export default function UserManagement(): React.ReactElement {
    const [users, setUsers] = useState<BizUserDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [pages, setPages] = useState(0);
    const [pageNo, setPageNo] = useState(1);
    const [allRoles, setAllRoles] = useState<ConfRoleDto[]>([]);
    const [userRoles, setUserRoles] = useState<number[]>([]);
    const [currentUserId, setCurrentUserId] = useState(0);
    const [allRolesVisible, setAllRolesVisible] = useState(false);
    const pageSize = 10;

    function displayFullName(firstName: string, lastName: string): string {
        return (firstName ? firstName : "") +
            (lastName ? " " + lastName : "");
    }

    function getUsers() {
        const params: GetUsersRequest = {
            pageNo,
            pageSize
        }

        getUsersApi(params)
            .then(response => {
                setTotalCount(response.resultBody.totalCount);
                setPages(Math.floor((response.resultBody.totalCount + pageSize - 1) / pageSize));
                setUsers(response.resultBody.dataList);
            }).catch(() => {});
    }

    function getAllRoles() {
        getAllRolesApi()
            .then(response => {
                setAllRoles(response.resultBody);
                getUserRolesApi(currentUserId).then(response => {
                    setUserRoles(response.resultBody.map(r => r.roleId));
                }).catch(() => {});
            }).catch(() => {});
    }

    function onRolesClick(userId: number):void {
        setCurrentUserId(userId);
        setAllRolesVisible(true);
    }

    function onRoleToggled(roleId: number, checked: boolean) {
        if (checked) {
            setUserRoles(userRoles.concat(roleId));
        }
        else {
            let index = userRoles.indexOf(roleId);
            setUserRoles(userRoles.slice(0, index).concat(userRoles.slice(index + 1)));
        }
    }

    function grantRoles() {
        grantRolesApi(currentUserId, userRoles)
            .then(() => {
                setAllRolesVisible(false);
            }).catch(() => {});
    }

    useEffect(() => {
        getUsers();
    }, [pageNo]);

    useEffect(() => {
        if (allRolesVisible) {
            getAllRoles();
        }
        else {
            setCurrentUserId(0);
            setAllRoles([]);
            setUserRoles([]);
        }
    }, [allRolesVisible]);

    return <div>
        <ThemeProvider theme={userTableTheme}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell style={{borderTopLeftRadius: "1rem", borderRight: "1px solid #B2B2B2"}}
                                   align={"center"}>Name</TableCell>
                        <TableCell style={{borderRight: "1px solid #B2B2B2"}}
                                   align={"center"}>Phone No.</TableCell>
                        <TableCell style={{borderTopRightRadius: "1rem"}} align={"center"}>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {users.map((user, index) => {
                        return <TableRow key={"UserRow-" + index}>
                            <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid " + colorGreyD2}} align={"center"}>{displayFullName(user.firstName, user.lastName)}</TableCell>
                            <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid " + colorGreyD2}} align={"center"}>{user.phoneNo}</TableCell>
                            <TableCell style={{fontSize: "1.5rem"}} align={"center"}>
                                <UserRolesButton onClick={() => onRolesClick(user.userId)}>Roles</UserRolesButton>
                            </TableCell>
                        </TableRow>;
                    })}
                </TableBody>
            </Table>
            <Pagination style={{float: "right", marginTop: "1rem"}} count={pages} page={pageNo}
                        onChange={(_e: React.ChangeEvent<unknown>, page: number) => {setPageNo(page)}}/>
            <UserTotalCount>{totalCount} in total</UserTotalCount>
        </ThemeProvider>
        <Dialog open={allRolesVisible}>
            <ThemeProvider theme={userRolesTheme}>
                <DialogTitle>Grant roles</DialogTitle>
                <DialogContent>
                    <FormGroup>
                        {allRoles.map((role, index) => {
                            return <FormControlLabel
                                key={"Role-" + index}
                                control={<Checkbox checked={userRoles.includes(role.roleId)}/>}
                                label={role.roleName}
                                onChange={(_e: React.SyntheticEvent, checked: boolean):void => onRoleToggled(role.roleId, checked)}/>
                        })}
                    </FormGroup>
                </DialogContent>
            </ThemeProvider>
            <DialogButtonWrapper>
                <CancellationButton onClick={() => setAllRolesVisible(false)}>Cancel</CancellationButton>
                <ConfirmationButton style={{marginRight: "1rem"}}
                     onClick={() => grantRoles()}>OK
                </ConfirmationButton>
            </DialogButtonWrapper>
        </Dialog>
    </div>
}