"use client"

import {useState} from "react"
import Link from "next/link"
import {Eye, EyeOff, Home, LogIn, UserPlus} from "lucide-react"
import api from "@/utils/baseApi";

import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"
import {useAuth} from "@/context/AuthContext";

interface LoginForm {
    email: string;
    password: string;
}

interface RegisterForm {
    email: string;
    password: string;
    name: string;
}

interface TokenResponse {
    access_token: string;
    refresh_token: string;
}

export default function AuthPage() {
    const [showPassword, setShowPassword] = useState(false);
    const [activeTab, setActiveTab] = useState("login");
    const [loginLoading, setLoginLoading] = useState(false);
    const [registerLoading, setRegisterLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const {login} = useAuth();
    const [loginData, setLoginData] = useState<LoginForm>({
        email: "",
        password: ""
    });
    const [registerData, setRegisterData] = useState<RegisterForm>({
        email: "",
        password: "",
        name: ""
    });

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handleLoginChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {id, value} = e.target;
        const field = id === "loginEmail" ? "email" : "password";
        setLoginData(prev => ({...prev, [field]: value}));
    };

    const handleRegisterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {id, value} = e.target;
        let field: keyof RegisterForm;
        if (id === "registerName") field = "name";
        else if (id === "registerEmail") field = "email";
        else field = "password";

        setRegisterData(prev => ({...prev, [field]: value}));
    };

    const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setLoginLoading(true);
        setError(null);

        try {
            const response = await fetch(`${api.baseURL}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(loginData)
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                setError(errorMessage || "Login failed. Please try again.");
                return;
            }

            const data: TokenResponse = await response.json();

            localStorage.setItem("authToken", data.access_token);
            localStorage.setItem("refreshToken", data.refresh_token);

            login(data.access_token);
        } catch (err) {
            console.error(err);
            setError("An unexpected error occurred. Please try again.");
        } finally {
            setLoginLoading(false);
        }
    };

    const handleRegister = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setRegisterLoading(true);
        setError(null);

        try {
            const response = await fetch(`${api.baseURL}/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(registerData)
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                setError(errorMessage || "Registration failed. Please try again.");
                return;
            }

            const data: TokenResponse = await response.json();

            localStorage.setItem("authToken", data.access_token);
            localStorage.setItem("refreshToken", data.refresh_token);

            login(data.access_token);
        } catch (err) {
            console.error(err);
            setError("An unexpected error occurred. Please try again.");
        } finally {
            setRegisterLoading(false);
        }
    };

    return (
        <div className="flex items-center justify-center p-20">
            <Card className="w-full max-w-md">
                <CardHeader className="space-y-1">
                    <CardTitle className="text-2xl font-bold text-center">Supply Chain Management</CardTitle>
                    <CardDescription className="text-center">
                        {activeTab === "login" ? "Login to your account" : "Create a new account"}
                    </CardDescription>
                </CardHeader>

                <CardContent>
                    <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
                        <TabsList className="grid w-full grid-cols-2 mb-6">
                            <TabsTrigger value="login" className="flex items-center gap-2">
                                <LogIn className="h-4 w-4"/>
                                Login
                            </TabsTrigger>
                            <TabsTrigger value="register" className="flex items-center gap-2">
                                <UserPlus className="h-4 w-4"/>
                                Register
                            </TabsTrigger>
                        </TabsList>

                        {error && <div className="text-red-500 text-center">{error}</div>}

                        <TabsContent value="login" className="space-y-4">
                            <form onSubmit={handleLogin} className="space-y-4">
                                <div className="space-y-2">
                                    <Label htmlFor="login-email">Email</Label>
                                    <Input
                                        id="loginEmail"
                                        onChange={handleLoginChange}
                                        name="login-email"
                                        type="email"
                                        placeholder="your.email@example.com"
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="login-password">Password</Label>
                                    <div className="relative">
                                        <Input
                                            id="loginPassword"
                                            onChange={handleLoginChange}
                                            type={showPassword ? "text" : "password"}
                                            placeholder="••••••••"
                                            required
                                        />
                                        <Button
                                            type="button"
                                            variant="ghost"
                                            size="icon"
                                            className="absolute right-2 top-1/2 -translate-y-1/2 h-8 w-8"
                                            onClick={togglePasswordVisibility}
                                        >
                                            {showPassword ? <EyeOff className="h-4 w-4"/> : <Eye className="h-4 w-4"/>}
                                        </Button>
                                    </div>
                                </div>
                                <Button type="submit" className="w-full" disabled={loginLoading}>
                                    {loginLoading ? "Logging in..." : "Login"}
                                </Button>
                            </form>
                        </TabsContent>

                        <TabsContent value="register" className="space-y-4">
                            <form onSubmit={handleRegister} className="space-y-4">
                                <div className="space-y-2">
                                    <Label htmlFor="name">Full Name</Label>
                                    <Input
                                        id="registerName"
                                        onChange={handleRegisterChange}
                                        placeholder="John Doe"
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="register-email">Email</Label>
                                    <Input
                                        id="registerEmail"
                                        onChange={handleRegisterChange}
                                        type="email"
                                        placeholder="your.email@example.com"
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="register-password">Password</Label>
                                    <div className="relative">
                                        <Input
                                            id="registerPassword"
                                            onChange={handleRegisterChange}
                                            type={showPassword ? "text" : "password"}
                                            placeholder="••••••••"
                                            required
                                        />
                                        <Button
                                            type="button"
                                            variant="ghost"
                                            size="icon"
                                            className="absolute right-2 top-1/2 -translate-y-1/2 h-8 w-8"
                                            onClick={togglePasswordVisibility}
                                        >
                                            {showPassword ? <EyeOff className="h-4 w-4"/> : <Eye className="h-4 w-4"/>}
                                        </Button>
                                    </div>
                                </div>
                                <Button type="submit" className="w-full" disabled={registerLoading}>
                                    {registerLoading ? "Registering..." : "Register"}
                                </Button>
                            </form>
                        </TabsContent>
                    </Tabs>
                </CardContent>

                <CardFooter className="flex flex-col space-y-4 items-center">
                    <div className="text-sm text-center">
                        By continuing, you agree to our Terms of Service and Privacy Policy.
                    </div>
                    <Button variant="outline" asChild>
                        <Link href="/" className="flex items-center gap-2">
                            <Home className="h-4 w-4" />
                            Return to Home
                        </Link>
                    </Button>
                </CardFooter>
            </Card>
        </div>
    );
}
