import {
    createTheme, FormControl,
    Grid, InputLabel, MenuItem,
    Pagination, Select,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    ThemeProvider
} from "@mui/material";
import {
    colorBlue,
    colorBlueDark, colorBlueDarker, colorBlueLight, colorGrey0B, colorGrey64,
    colorGreyD2,
    colorOrange,
    colorOrangeLight,
    colorSuccess,
    colorSuccessLight
} from "../../utils/constant";
import React, {useEffect, useState} from "react";
import styled from "styled-components";
import {getPermissionsApi} from "../../api/permissionApi";
import {GetPermissionsRequest} from "../../model/request";
import {ConfPermissionDto} from "../../model/dto";

const UrlInput = styled.input`
    width: calc(100% - 1rem);
    height: 100%;
    margin-left: 1rem;
    padding: 0 0.5rem;
    color: ${() => {
        return colorGrey64;
    }};
    font-size: 1.5rem;
    border: ${() => {
        return "1px solid " + colorGreyD2;
    }};
    border-radius: 0.5rem;
    box-sizing: border-box;
`

const SearchButton = styled.div`
    display: flex;
    width: 62%;
    height: 100%;
    margin-left: 1rem;
    background-color: ${() => {
        return colorBlue;
    }};
    border-radius: 0.5rem;
    align-items: center;
    box-shadow: ${() => {
        return "0 0.1rem 0.1rem " + colorGrey64;
    }};
    &:hover {
        cursor: pointer;
        background-color: ${() => {
            return colorBlueDark;
        }};
    }
    &:active {
        background-color: ${() => {
            return colorBlueDarker;
        }};
    }
`

const SearchButtonText = styled.div`
    width: 100%;
    color: ${() => {
        return colorGrey0B;
    }};
    font-size: 1.5rem;
    text-align: center;
`

type HttpMethodProps = {
    method: string
}
const HttpMethod = styled.span<HttpMethodProps>`
    color: ${props => {
    if (props.method === "PUT") {
        return colorBlue;
    }
    else if (props.method === "POST") {
        return colorOrange;
    }
    else {
        // GET
        return colorSuccess;
    }
}};
    font-weight: bold;
    border-width: 0.1rem;
    border-style: solid;
    border-color: ${props => {
    if (props.method === "PUT") {
        return colorBlue;
    }
    else if (props.method === "POST") {
        return colorOrange;
    }
    else {
        // GET
        return colorSuccess;
    }
}};
    border-radius: 0.5rem;
    padding: 0.1rem 0.5rem;
    background-color: ${props => {
    if (props.method === "PUT") {
        return colorBlueLight;
    }
    else if (props.method === "POST") {
        return colorOrangeLight;
    }
    else {
        // GET
        return colorSuccessLight;
    }
}};
`

const PermissionTotalCount = styled.span`
    float: right;
    margin-top: 1rem;
    font-size: 1.5rem;
    padding: 0.6rem 0;
`

const queryFormTheme = createTheme({
    components: {
        MuiGrid: {
            styleOverrides: {
                root: {
                    width: "100%",
                    marginLeft: 0,
                    marginTop: 0,
                    padding: 0,
                    "&.MuiGrid-item": {
                        height: "4rem",
                        padding: 0
                    }
                }
            }
        },
        MuiFormControl: {
            styleOverrides: {
                root: {
                    height: "100%"
                }
            }
        },
        MuiSelect: {
            styleOverrides: {
                root: {
                    height: "100%",
                    fontSize: "1.5rem"
                }
            }
        },
        MuiMenuItem: {
            styleOverrides: {
                root: {
                    fontSize: "1.5rem"
                }
            }
        }
    }
});

const permissionTableTheme = createTheme({
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
                    color: colorGrey0B,
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

export default function PermissionManagement(): React.ReactElement {
    const httpMethods = [
        {
            label: "All",
            method: "ALL"
        },
        {
            label: "GET",
            method: "GET"
        },
        {
            label: "PUT",
            method: "PUT"
        },
        {
            label: "POST",
            method: "POST"
        }
    ];

    const [httpMethod, setHttpMethod] = useState("ALL");
    const [permissionUrl, setPermissionUrl] = useState("");
    const [permissions, setPermissions] = useState<ConfPermissionDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [pages, setPages] = useState(0);
    const [pageNo, setPageNo] = useState(1);
    let pageSize = 10;

    function searchForPermissions() {
        const params: GetPermissionsRequest = {
            pageNo,
            pageSize
        }

        if (httpMethod !== "ALL") {
            params.httpMethod = httpMethod;
        }
        if (permissionUrl && permissionUrl.trim()) {
            params.permissionUrl = permissionUrl;
        }
        getPermissionsApi(params).then(response => {
            setTotalCount(response.resultBody.totalCount);
            setPages(Math.floor((response.resultBody.totalCount + pageSize - 1) / pageSize));
            setPermissions(response.resultBody.dataList);
        }).catch(() => {});
    }

    useEffect(() => {
        searchForPermissions();
    }, [pageNo]);

    return <div>
        <ThemeProvider theme={queryFormTheme}>
            <Grid container spacing={"1rem"}>
                <Grid item xs={4}>
                    <FormControl fullWidth>
                        <InputLabel id={"httpMethod"}>HTTP Method</InputLabel>
                        <Select
                            labelId={"httpMethod"}
                            value={httpMethod}
                            onChange={e => {setHttpMethod(e.target.value)}}>
                            {httpMethods.map(m => {
                                return <MenuItem value={m.method}>{m.label}</MenuItem>
                            })}
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item xs={4}>
                    <UrlInput placeholder={"URL Prefix"} onChange={e => setPermissionUrl(e.target.value)}/>
                </Grid>
                <Grid item xs={2}>
                    <SearchButton onClick={() => searchForPermissions()}><SearchButtonText>Search</SearchButtonText></SearchButton>
                </Grid>
            </Grid>
        </ThemeProvider>
        <div style={{height: "1rem"}}></div>
        <ThemeProvider theme={permissionTableTheme}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell style={{borderTopLeftRadius: "1rem", borderRight: "1px solid #B2B2B2"}}
                                   align={"center"}>HTTP Method</TableCell>
                        <TableCell style={{borderTopRightRadius: "1rem"}}
                                   align={"center"}>URL</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {permissions.map((permission, index) => {
                        return <TableRow key={"UserRow-" + index}>
                            <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid " + colorGreyD2}} align={"right"}>
                                <HttpMethod method={permission.httpMethod}>{permission.httpMethod}</HttpMethod>
                            </TableCell>
                            <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid " + colorGreyD2}} align={"left"}>{permission["permissionUrl"]}</TableCell>
                        </TableRow>;
                    })}
                </TableBody>
            </Table>
            <Pagination style={{float: "right", marginTop: "1rem"}} count={pages} page={pageNo}
                        onChange={(e, page) => {setPageNo(page)}}/>
            <PermissionTotalCount>{totalCount} in total</PermissionTotalCount>
        </ThemeProvider>
    </div>
}